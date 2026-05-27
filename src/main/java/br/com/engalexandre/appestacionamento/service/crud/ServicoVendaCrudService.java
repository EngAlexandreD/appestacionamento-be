package br.com.engalexandre.appestacionamento.service.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ServicoVendaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoVendaResponse;
import br.com.engalexandre.appestacionamento.entity.ServicoVendaEntity;
import br.com.engalexandre.appestacionamento.repository.ServicoVendaRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class ServicoVendaCrudService {

    private static final String ENTITY_TYPE = "servico-venda";

    private final ServicoVendaRepository repository;
    private final DeletionLogService deletionLogService;

    public ServicoVendaCrudService(ServicoVendaRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<ServicoVendaResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ServicoVendaResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ServicoVendaResponse create(ServicoVendaRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        ServicoVendaEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }
        return persistNew(request);
    }

    @Transactional
    public ServicoVendaResponse update(String id, ServicoVendaRequest request) {
        ServicoVendaEntity entity = getEntity(id);
        apply(entity, request);
        ServicoVendaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public ServicoVendaResponse upsertByExternalId(String externalId, ServicoVendaRequest request) {
        ServicoVendaEntity entity = repository.findByExternalId(externalId).orElseGet(ServicoVendaEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        ServicoVendaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        ServicoVendaEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        ServicoVendaEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda de serviço não encontrada."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private ServicoVendaResponse persistNew(ServicoVendaRequest request) {
        ServicoVendaEntity entity = new ServicoVendaEntity();
        apply(entity, request);
        ServicoVendaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    private ServicoVendaEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda de serviço não encontrada."));
    }

    private void apply(ServicoVendaEntity entity, ServicoVendaRequest request) {
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank())
                && request.externalId() != null
                && !request.externalId().isBlank()) {
            entity.setExternalId(request.externalId());
        }
        entity.setServicoExternalId(request.servicoExternalId());
        entity.setNome(request.nome().trim());
        entity.setValor(request.valor());
        entity.setMetodo(request.metodo().trim());
        entity.setData(request.data() == null ? Instant.now() : request.data());
    }

    private ServicoVendaResponse toResponse(ServicoVendaEntity entity) {
        return new ServicoVendaResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getServicoExternalId(),
                entity.getNome(),
                entity.getValor(),
                entity.getMetodo(),
                entity.getData(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}