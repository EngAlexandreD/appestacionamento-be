package br.com.engalexandre.appestacionamento.service.crud;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.crud.VagaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.VagaResponse;
import br.com.engalexandre.appestacionamento.entity.VagaEntity;
import br.com.engalexandre.appestacionamento.repository.VagaRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;

@Service
public class VagaCrudService {

    private static final String ENTITY_TYPE = "vaga";

    private final VagaRepository repository;
    private final DeletionLogService deletionLogService;

    public VagaCrudService(VagaRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<VagaResponse> listAll() {
        return repository.findAll().stream()
                .sorted((left, right) -> Integer.compare(left.getNumero(), right.getNumero()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VagaResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public VagaResponse create(VagaRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        VagaEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }

        return persistNew(request);
    }

    private VagaResponse persistNew(VagaRequest request) {
        repository.findByNumero(request.numero()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe vaga com este número.");
        });
        VagaEntity entity = new VagaEntity();
        apply(entity, request);
        VagaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public VagaResponse update(String id, VagaRequest request) {
        VagaEntity entity = getEntity(id);
        repository.findByNumero(request.numero()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe vaga com este número.");
            }
        });
        apply(entity, request);
        VagaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        VagaEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public VagaResponse upsertByExternalId(String externalId, VagaRequest request) {
        VagaEntity entity = repository.findByExternalId(externalId).orElseGet(VagaEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        VagaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        VagaEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private VagaEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada."));
    }

    private void apply(VagaEntity entity, VagaRequest request) {
        String externalId = request.externalId();
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank()) && externalId != null && !externalId.isBlank()) {
            entity.setExternalId(externalId);
        }
        entity.setNumero(request.numero());
        entity.setOcupada(Boolean.TRUE.equals(request.ocupada()));
        entity.setMensalista(Boolean.TRUE.equals(request.mensalista()));
        entity.setEntrada(request.entrada());
        entity.setPlaca(request.placa());
        entity.setTicketId(request.ticketId());
        entity.setTicketCode(request.ticketCode());
        entity.setTipoVeiculo(request.tipoVeiculo());
        entity.setOperador(request.operador());
    }

    private VagaResponse toResponse(VagaEntity entity) {
        return new VagaResponse(
                entity.getId(),
            entity.getExternalId(),
                entity.getNumero(),
                entity.isOcupada(),
                entity.isMensalista(),
                entity.getEntrada(),
                entity.getPlaca(),
                entity.getTicketId(),
                entity.getTicketCode(),
                entity.getTipoVeiculo(),
                entity.getOperador(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}