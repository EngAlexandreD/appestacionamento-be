package br.com.engalexandre.appestacionamento.service.crud;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRecebimentoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRecebimentoResponse;
import br.com.engalexandre.appestacionamento.entity.ConvenioEntity;
import br.com.engalexandre.appestacionamento.entity.ConvenioRecebimentoEntity;
import br.com.engalexandre.appestacionamento.repository.ConvenioRecebimentoRepository;
import br.com.engalexandre.appestacionamento.repository.ConvenioRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import jakarta.transaction.Transactional;

@Service
public class ConvenioRecebimentoCrudService {

    private static final String ENTITY_TYPE = "convenio_recebimento";

    private final ConvenioRepository convenioRepository;
    private final ConvenioRecebimentoRepository repository;
    private final DeletionLogService deletionLogService;

    public ConvenioRecebimentoCrudService(
            ConvenioRepository convenioRepository,
            ConvenioRecebimentoRepository repository,
            DeletionLogService deletionLogService
    ) {
        this.convenioRepository = convenioRepository;
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional
    public ConvenioRecebimentoResponse registerRecebimento(
            String convenioExternalId,
            ConvenioRecebimentoRequest request
    ) {
        ConvenioEntity convenio = findConvenioByExternalId(convenioExternalId);
        ConvenioRecebimentoEntity entity = new ConvenioRecebimentoEntity();
        entity.setExternalId(request.id() == null || request.id().isBlank()
                ? String.valueOf(System.nanoTime())
                : request.id());
        entity.setConvenio(convenio);
        apply(entity, request);

        ConvenioRecebimentoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public ConvenioRecebimentoResponse upsertByExternalIds(
            String convenioExternalId,
            String recebimentoExternalId,
            ConvenioRecebimentoRequest request
    ) {
        ConvenioEntity convenio = findConvenioByExternalId(convenioExternalId);

        ConvenioRecebimentoEntity entity = repository.findByExternalId(recebimentoExternalId)
                .orElseGet(() -> {
                    ConvenioRecebimentoEntity novo = new ConvenioRecebimentoEntity();
                    novo.setExternalId(recebimentoExternalId);
                    novo.setConvenio(convenio);
                    return novo;
                });

        if (entity.getConvenio() == null) {
            entity.setConvenio(convenio);
        }

        apply(entity, request);
        entity.setExternalId(recebimentoExternalId);

        ConvenioRecebimentoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        ConvenioRecebimentoEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recebimento de convênio não encontrado."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    public List<ConvenioRecebimentoResponse> listAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private ConvenioEntity findConvenioByExternalId(String externalId) {
        return convenioRepository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado."));
    }

    private void apply(ConvenioRecebimentoEntity entity, ConvenioRecebimentoRequest request) {
        entity.setDataHora(request.dataHora() == null ? Instant.now() : request.dataHora());
        entity.setValorPago(request.valorPago());
        entity.setMetodoPagamento(request.metodoPagamento() == null || request.metodoPagamento().isBlank()
                ? "Pix"
                : request.metodoPagamento());
        entity.setObservacao(request.observacao());
    }

    private ConvenioRecebimentoResponse toResponse(ConvenioRecebimentoEntity entity) {
        return new ConvenioRecebimentoResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getConvenio().getExternalId(),
                entity.getConvenio().getNome(),
                entity.getDataHora(),
                entity.getValorPago(),
                entity.getMetodoPagamento(),
                entity.getObservacao(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
