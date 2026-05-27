package br.com.engalexandre.appestacionamento.service.crud;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.crud.MensalistaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.MensalistaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.PagamentoMensalistaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.PagamentoMensalistaResponse;
import br.com.engalexandre.appestacionamento.entity.MensalistaEntity;
import br.com.engalexandre.appestacionamento.entity.PagamentoMensalistaEntity;
import br.com.engalexandre.appestacionamento.repository.MensalistaRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;

@Service
public class MensalistaCrudService {

    private static final String ENTITY_TYPE = "mensalista";

    private final MensalistaRepository repository;
    private final DeletionLogService deletionLogService;

    public MensalistaCrudService(MensalistaRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<MensalistaResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MensalistaResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public MensalistaResponse create(MensalistaRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            Optional<MensalistaEntity> existing = repository.findByExternalId(request.externalId());
            if (existing.isPresent()) {
                apply(existing.get(), request);
                MensalistaEntity saved = repository.save(existing.get());
                deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                return toResponse(saved);
            }
        }

        MensalistaEntity entity = new MensalistaEntity();
        apply(entity, request);
        MensalistaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public MensalistaResponse update(String id, MensalistaRequest request) {
        MensalistaEntity entity = getEntity(id);
        apply(entity, request);
        MensalistaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        MensalistaEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public MensalistaResponse upsertByExternalId(String externalId, MensalistaRequest request) {
        MensalistaEntity entity = repository.findByExternalId(externalId).orElseGet(MensalistaEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        MensalistaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        MensalistaEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensalista não encontrado."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public MensalistaResponse registerPayment(String id, PagamentoMensalistaRequest request) {
        MensalistaEntity entity = getEntity(id);
        PagamentoMensalistaEntity pagamento = new PagamentoMensalistaEntity();
        pagamento.setExternalId(request.externalId() == null || request.externalId().isBlank() ? String.valueOf(System.nanoTime()) : request.externalId());
        pagamento.setDataHora(request.dataHora() == null ? Instant.now() : request.dataHora());
        pagamento.setValorPago(request.valorPago());
        entity.adicionarPagamento(pagamento);
        MensalistaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public MensalistaResponse upsertPaymentByExternalIds(
            String mensalistaExternalId,
            String pagamentoExternalId,
            PagamentoMensalistaRequest request
    ) {
        MensalistaEntity entity = repository.findByExternalId(mensalistaExternalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensalista não encontrado."));

        PagamentoMensalistaEntity pagamento = entity.getPagamentos().stream()
                .filter(item -> pagamentoExternalId.equals(item.getExternalId()))
                .findFirst()
                .orElseGet(() -> {
                    PagamentoMensalistaEntity novo = new PagamentoMensalistaEntity();
                    novo.setExternalId(pagamentoExternalId);
                    entity.adicionarPagamento(novo);
                    return novo;
                });

        pagamento.setDataHora(request.dataHora() == null ? Instant.now() : request.dataHora());
        pagamento.setValorPago(request.valorPago());

        MensalistaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    private MensalistaEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensalista não encontrado."));
    }

    private void apply(MensalistaEntity entity, MensalistaRequest request) {
        String externalId = request.externalId();
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank()) && externalId != null && !externalId.isBlank()) {
            entity.setExternalId(externalId);
        }
        entity.setNome(request.nome().trim());
        entity.setPlaca(request.placa());
        entity.setTelefone(request.telefone());
        entity.setValorMensalidade(request.valorMensalidade());
        entity.setDiaVencimento(request.diaVencimento());
        entity.setAtivo(request.ativo() == null || request.ativo());
    }

    private MensalistaResponse toResponse(MensalistaEntity entity) {
        return new MensalistaResponse(
                entity.getId(),
            entity.getExternalId(),
                entity.getNome(),
                entity.getPlaca(),
                entity.getTelefone(),
                entity.getValorMensalidade(),
                entity.getDiaVencimento(),
                entity.isAtivo(),
                entity.getPagamentos().stream().map(this::toPaymentResponse).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private PagamentoMensalistaResponse toPaymentResponse(PagamentoMensalistaEntity pagamento) {
        return new PagamentoMensalistaResponse(
                pagamento.getId(),
            pagamento.getExternalId(),
                pagamento.getDataHora(),
                pagamento.getValorPago(),
                pagamento.getCreatedAt(),
                pagamento.getUpdatedAt()
        );
    }
}