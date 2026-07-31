package br.com.engalexandre.appestacionamento.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.config.PropriedadesPagBank;
import br.com.engalexandre.appestacionamento.dto.crud.PixCobrancaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.PixCobrancaResponse;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankChargeResponse;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankOrderResponse;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankQrCodeResponse;
import br.com.engalexandre.appestacionamento.entity.PixCobrancaEntity;
import br.com.engalexandre.appestacionamento.entity.PixCobrancaStatus;
import br.com.engalexandre.appestacionamento.repository.PixCobrancaRepository;

@Service
public class PixCobrancaService {

    private static final Logger log = LoggerFactory.getLogger(PixCobrancaService.class);

    private final PixCobrancaRepository repository;
    private final PagBankClient pagBankClient;
    private final PropriedadesPagBank propriedades;

    public PixCobrancaService(PixCobrancaRepository repository, PagBankClient pagBankClient,
            PropriedadesPagBank propriedades) {
        this.repository = repository;
        this.pagBankClient = pagBankClient;
        this.propriedades = propriedades;
    }

    @Transactional
    public PixCobrancaResponse criar(PixCobrancaRequest request) {
        // Idempotente: se o app reenviar a mesma referencia (ex.: apos timeout sem
        // saber se a primeira chamada emplacou), nao cria uma segunda ordem no PagBank.
        Optional<PixCobrancaEntity> existente = repository.findByReferenciaOrigemId(request.referenciaOrigemId());
        if (existente.isPresent()) {
            return toResponse(existente.get());
        }

        Instant expiraEm = Instant.now().plusSeconds(propriedades.getExpiracaoSegundos());
        PagBankOrderResponse ordem = pagBankClient.criarOrdemPix(request.valor(), request.referenciaOrigemId(), expiraEm);

        String qrTexto = ordem.qrCodes() == null || ordem.qrCodes().isEmpty()
                ? null
                : ordem.qrCodes().stream().map(PagBankQrCodeResponse::text).findFirst().orElse(null);

        PixCobrancaEntity entity = new PixCobrancaEntity();
        entity.setReferenciaOrigemId(request.referenciaOrigemId());
        entity.setOrigemTipo(request.origemTipo());
        entity.setValor(request.valor());
        entity.setStatus(PixCobrancaStatus.AGUARDANDO);
        entity.setPagbankOrderId(ordem.id());
        entity.setQrCodeTexto(qrTexto);
        entity.setExpiraEm(expiraEm);

        return toResponse(repository.save(entity));
    }

    @Transactional
    public PixCobrancaResponse consultarStatusAtual(String cobrancaId) {
        PixCobrancaEntity entity = getEntity(cobrancaId);

        if (entity.getStatus() == PixCobrancaStatus.AGUARDANDO
                && entity.getExpiraEm() != null
                && Instant.now().isAfter(entity.getExpiraEm())) {
            entity.setStatus(PixCobrancaStatus.EXPIRADO);
            entity = repository.save(entity);
        }

        return toResponse(entity);
    }

    // Chamado apenas pelo webhook. Nunca confia no corpo da notificacao — sempre
    // reconsulta o PagBank com nosso proprio token antes de gravar qualquer status.
    @Transactional
    public void processarNotificacaoWebhook(String pagbankOrderId) {
        Optional<PixCobrancaEntity> encontrada = repository.findByPagbankOrderId(pagbankOrderId);
        if (encontrada.isEmpty()) {
            log.info("Webhook do PagBank recebido para ordem desconhecida: {}", pagbankOrderId);
            return;
        }

        PixCobrancaEntity entity = encontrada.get();
        if (isStatusTerminal(entity.getStatus())) {
            // Idempotente: PagBank pode reenviar o mesmo webhook varias vezes.
            return;
        }

        PagBankOrderResponse ordem = pagBankClient.consultarOrdem(pagbankOrderId);
        List<PagBankChargeResponse> charges = ordem.charges();
        String statusPagBank = (charges == null || charges.isEmpty()) ? null : charges.get(0).status();

        PixCobrancaStatus novoStatus = mapearStatus(statusPagBank);
        if (novoStatus == null || novoStatus == entity.getStatus()) {
            return;
        }

        entity.setStatus(novoStatus);
        if (novoStatus == PixCobrancaStatus.PAGO) {
            entity.setPagoEm(Instant.now());
        }
        repository.save(entity);
    }

    private PixCobrancaStatus mapearStatus(String statusPagBank) {
        if (statusPagBank == null) {
            return null;
        }
        return switch (statusPagBank) {
            case "PAID" -> PixCobrancaStatus.PAGO;
            case "DECLINED", "CANCELED" -> PixCobrancaStatus.CANCELADO;
            default -> null; // WAITING/IN_ANALYSIS: continua aguardando, nada a atualizar
        };
    }

    private boolean isStatusTerminal(PixCobrancaStatus status) {
        return status == PixCobrancaStatus.PAGO
                || status == PixCobrancaStatus.CANCELADO
                || status == PixCobrancaStatus.EXPIRADO;
    }

    private PixCobrancaEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cobranca PIX nao encontrada."));
    }

    private PixCobrancaResponse toResponse(PixCobrancaEntity entity) {
        return new PixCobrancaResponse(
                entity.getId(),
                entity.getStatus().name(),
                entity.getValor(),
                entity.getQrCodeTexto(),
                entity.getExpiraEm(),
                entity.getPagoEm()
        );
    }
}
