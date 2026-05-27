package br.com.engalexandre.appestacionamento.service.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ProdutoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoResponse;
import br.com.engalexandre.appestacionamento.entity.ProdutoEntity;
import br.com.engalexandre.appestacionamento.repository.ProdutoRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoCrudService {

    private static final String ENTITY_TYPE = "produto";

    private final ProdutoRepository repository;
    private final DeletionLogService deletionLogService;

    public ProdutoCrudService(ProdutoRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ProdutoResponse create(ProdutoRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        ProdutoEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }
        return persistNew(request);
    }

    @Transactional
    public ProdutoResponse update(String id, ProdutoRequest request) {
        ProdutoEntity entity = getEntity(id);
        apply(entity, request);
        ProdutoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public ProdutoResponse upsertByExternalId(String externalId, ProdutoRequest request) {
        ProdutoEntity entity = repository.findByExternalId(externalId).orElseGet(ProdutoEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        ProdutoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        ProdutoEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        ProdutoEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private ProdutoResponse persistNew(ProdutoRequest request) {
        ProdutoEntity entity = new ProdutoEntity();
        apply(entity, request);
        ProdutoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    private ProdutoEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
    }

    private void apply(ProdutoEntity entity, ProdutoRequest request) {
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank())
                && request.externalId() != null
                && !request.externalId().isBlank()) {
            entity.setExternalId(request.externalId());
        }
        entity.setNome(request.nome().trim());
        entity.setQuantidade(request.quantidade());
        entity.setValorPago(request.valorPago());
        entity.setMargemLucro(request.margemLucro());
        entity.setValorVenda(request.valorVenda());
        entity.setCodigoBarras(request.codigoBarras());
    }

    private ProdutoResponse toResponse(ProdutoEntity entity) {
        return new ProdutoResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getNome(),
                entity.getQuantidade(),
                entity.getValorPago(),
                entity.getMargemLucro(),
                entity.getValorVenda(),
                entity.getCodigoBarras(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}