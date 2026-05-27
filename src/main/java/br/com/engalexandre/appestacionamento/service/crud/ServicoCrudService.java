package br.com.engalexandre.appestacionamento.service.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ServicoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoResponse;
import br.com.engalexandre.appestacionamento.entity.ServicoEntity;
import br.com.engalexandre.appestacionamento.repository.ServicoRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicoCrudService {

    private static final String ENTITY_TYPE = "servico";

    private final ServicoRepository repository;
    private final DeletionLogService deletionLogService;

    public ServicoCrudService(ServicoRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<ServicoResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ServicoResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ServicoResponse create(ServicoRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        ServicoEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }
        return persistNew(request);
    }

    @Transactional
    public ServicoResponse update(String id, ServicoRequest request) {
        ServicoEntity entity = getEntity(id);
        apply(entity, request);
        ServicoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public ServicoResponse upsertByExternalId(String externalId, ServicoRequest request) {
        ServicoEntity entity = repository.findByExternalId(externalId).orElseGet(ServicoEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        ServicoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        ServicoEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        ServicoEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private ServicoResponse persistNew(ServicoRequest request) {
        ServicoEntity entity = new ServicoEntity();
        apply(entity, request);
        ServicoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    private ServicoEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
    }

    private void apply(ServicoEntity entity, ServicoRequest request) {
        Integer cor = request.cor();
        Integer ordem = request.ordem();
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank())
                && request.externalId() != null
                && !request.externalId().isBlank()) {
            entity.setExternalId(request.externalId());
        }
        entity.setNome(request.nome().trim());
        entity.setPreco(request.preco());
        if (cor != null) {
            entity.setCor(cor);
        } else {
            entity.setCor(0xFF1E88E5);
        }
        if (ordem != null) {
            entity.setOrdem(ordem);
        } else {
            entity.setOrdem(0);
        }
        entity.setAtivo(request.ativo() == null || request.ativo());
    }

    private ServicoResponse toResponse(ServicoEntity entity) {
        return new ServicoResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getNome(),
                entity.getPreco(),
                entity.getCor(),
                entity.getOrdem(),
                entity.isAtivo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}