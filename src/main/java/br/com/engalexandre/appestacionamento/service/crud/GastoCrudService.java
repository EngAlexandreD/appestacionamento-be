package br.com.engalexandre.appestacionamento.service.crud;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.crud.GastoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.GastoResponse;
import br.com.engalexandre.appestacionamento.entity.GastoEntity;
import br.com.engalexandre.appestacionamento.repository.GastoRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;

@Service
public class GastoCrudService {

    private final GastoRepository repository;
    private final DeletionLogService deletionLogService;

    public GastoCrudService(GastoRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<GastoResponse> listAll() {
        return repository.findAllByOrderByDataHoraDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GastoResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public GastoResponse upsert(GastoRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        GastoEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker("gasto", saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }
        return persistNew(request);
    }

    private GastoResponse persistNew(GastoRequest request) {
        GastoEntity entity = new GastoEntity();
        apply(entity, request);
        GastoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker("gasto", saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String externalId) {
        GastoEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto nao encontrado."));
        repository.delete(entity);
        deletionLogService.recordDeletion("gasto", externalId);
    }

    private GastoEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto nao encontrado."));
    }

    private void apply(GastoEntity entity, GastoRequest request) {
        entity.setExternalId(request.externalId());
        entity.setDescricao(request.descricao().trim());
        entity.setCategoria(request.categoria().trim());
        entity.setValor(request.valor());
        entity.setObservacao(request.observacao() != null ? request.observacao().trim() : null);
        entity.setDataHora(request.dataHora() == null ? Instant.now() : request.dataHora());
    }

    private GastoResponse toResponse(GastoEntity entity) {
        return new GastoResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getDescricao(),
                entity.getCategoria(),
                entity.getValor(),
                entity.getObservacao(),
                entity.getDataHora(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
