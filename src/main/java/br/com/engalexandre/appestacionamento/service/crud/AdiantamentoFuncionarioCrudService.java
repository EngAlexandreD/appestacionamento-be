package br.com.engalexandre.appestacionamento.service.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.crud.AdiantamentoFuncionarioRequest;
import br.com.engalexandre.appestacionamento.dto.crud.AdiantamentoFuncionarioResponse;
import br.com.engalexandre.appestacionamento.entity.AdiantamentoFuncionarioEntity;
import br.com.engalexandre.appestacionamento.repository.AdiantamentoFuncionarioRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import jakarta.transaction.Transactional;

@Service
public class AdiantamentoFuncionarioCrudService {

    private static final String ENTITY_TYPE = "adiantamento";

    private final AdiantamentoFuncionarioRepository repository;
    private final DeletionLogService deletionLogService;

    public AdiantamentoFuncionarioCrudService(
            AdiantamentoFuncionarioRepository repository,
            DeletionLogService deletionLogService
    ) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<AdiantamentoFuncionarioResponse> listAll() {
        return repository.findAllByOrderByDataHoraDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public AdiantamentoFuncionarioResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public AdiantamentoFuncionarioResponse create(AdiantamentoFuncionarioRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            Optional<AdiantamentoFuncionarioEntity> existing = repository.findByExternalId(request.externalId());
            if (existing.isPresent()) {
                apply(existing.get(), request);
                AdiantamentoFuncionarioEntity saved = repository.save(existing.get());
                deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                return toResponse(saved);
            }
        }

        AdiantamentoFuncionarioEntity entity = new AdiantamentoFuncionarioEntity();
        apply(entity, request);
        AdiantamentoFuncionarioEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public AdiantamentoFuncionarioResponse update(String id, AdiantamentoFuncionarioRequest request) {
        AdiantamentoFuncionarioEntity entity = getEntity(id);
        apply(entity, request);
        AdiantamentoFuncionarioEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        AdiantamentoFuncionarioEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public AdiantamentoFuncionarioResponse upsertByExternalId(String externalId, AdiantamentoFuncionarioRequest request) {
        AdiantamentoFuncionarioEntity entity = repository.findByExternalId(externalId)
                .orElseGet(AdiantamentoFuncionarioEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        AdiantamentoFuncionarioEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        AdiantamentoFuncionarioEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adiantamento não encontrado."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private AdiantamentoFuncionarioEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adiantamento não encontrado."));
    }

    private void apply(AdiantamentoFuncionarioEntity entity, AdiantamentoFuncionarioRequest request) {
        if (entity.getExternalId() == null || entity.getExternalId().isBlank()) {
            if (request.externalId() != null && !request.externalId().isBlank()) {
                entity.setExternalId(request.externalId());
            } else {
                entity.setExternalId(java.util.UUID.randomUUID().toString());
            }
        }
        entity.setFuncionarioNome(request.funcionarioNome().trim());
        entity.setDescricao(request.descricao().trim());
        entity.setDataHora(request.dataHora() == null ? java.time.Instant.now() : request.dataHora());
        entity.setValor(request.valor());
    }

    private AdiantamentoFuncionarioResponse toResponse(AdiantamentoFuncionarioEntity entity) {
        return new AdiantamentoFuncionarioResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getFuncionarioNome(),
                entity.getDescricao(),
                entity.getDataHora(),
                entity.getValor(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
