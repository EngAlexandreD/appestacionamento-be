package br.com.engalexandre.appestacionamento.service.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ProdutoVendaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoVendaResponse;
import br.com.engalexandre.appestacionamento.entity.ProdutoVendaEntity;
import br.com.engalexandre.appestacionamento.repository.ProdutoVendaRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class ProdutoVendaCrudService {

    private static final String ENTITY_TYPE = "produto-venda";

    private final ProdutoVendaRepository repository;
    private final DeletionLogService deletionLogService;

    public ProdutoVendaCrudService(ProdutoVendaRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<ProdutoVendaResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoVendaResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ProdutoVendaResponse create(ProdutoVendaRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        ProdutoVendaEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }
        return persistNew(request);
    }

    @Transactional
    public ProdutoVendaResponse update(String id, ProdutoVendaRequest request) {
        ProdutoVendaEntity entity = getEntity(id);
        apply(entity, request);
        ProdutoVendaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public ProdutoVendaResponse upsertByExternalId(String externalId, ProdutoVendaRequest request) {
        ProdutoVendaEntity entity = repository.findByExternalId(externalId).orElseGet(ProdutoVendaEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        ProdutoVendaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        ProdutoVendaEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        ProdutoVendaEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda de produto não encontrada."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private ProdutoVendaResponse persistNew(ProdutoVendaRequest request) {
        ProdutoVendaEntity entity = new ProdutoVendaEntity();
        apply(entity, request);
        ProdutoVendaEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    private ProdutoVendaEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda de produto não encontrada."));
    }

    private void apply(ProdutoVendaEntity entity, ProdutoVendaRequest request) {
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank())
                && request.externalId() != null
                && !request.externalId().isBlank()) {
            entity.setExternalId(request.externalId());
        }
        entity.setProdutoExternalId(request.produtoExternalId());
        entity.setNome(request.nome().trim());
        entity.setQuantidade(request.quantidade());
        entity.setValorVenda(request.valorVenda());
        entity.setData(request.data() == null ? Instant.now() : request.data());
    }

    private ProdutoVendaResponse toResponse(ProdutoVendaEntity entity) {
        return new ProdutoVendaResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getProdutoExternalId(),
                entity.getNome(),
                entity.getQuantidade(),
                entity.getValorVenda(),
                entity.getData(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}