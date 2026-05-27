package br.com.engalexandre.appestacionamento.service.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ConvenioFaixaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioFaixaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioResponse;
import br.com.engalexandre.appestacionamento.entity.ConvenioEntity;
import br.com.engalexandre.appestacionamento.entity.ConvenioFaixaEntity;
import br.com.engalexandre.appestacionamento.entity.ConvenioTipo;
import br.com.engalexandre.appestacionamento.repository.ConvenioRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConvenioCrudService {

    private static final String ENTITY_TYPE = "convenio";

    private final ConvenioRepository repository;
    private final DeletionLogService deletionLogService;

    public ConvenioCrudService(ConvenioRepository repository, DeletionLogService deletionLogService) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<ConvenioResponse> listAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ConvenioResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public ConvenioResponse create(ConvenioRequest request) {
        if (request.externalId() != null && !request.externalId().isBlank()) {
            return repository.findByExternalId(request.externalId())
                    .map(existing -> {
                        apply(existing, request);
                        ConvenioEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }

        return persistNew(request);
    }

    private ConvenioResponse persistNew(ConvenioRequest request) {
        if (repository.existsByNomeIgnoreCase(request.nome().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe convênio com este nome.");
        }
        ConvenioEntity entity = new ConvenioEntity();
        apply(entity, request);
        ConvenioEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public ConvenioResponse update(String id, ConvenioRequest request) {
        ConvenioEntity entity = getEntity(id);
        boolean sameName = entity.getNome().equalsIgnoreCase(request.nome().trim());
        if (!sameName && repository.existsByNomeIgnoreCase(request.nome().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe convênio com este nome.");
        }
        apply(entity, request);
        ConvenioEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        ConvenioEntity entity = getEntity(id);
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    @Transactional
    public ConvenioResponse upsertByExternalId(String externalId, ConvenioRequest request) {
        ConvenioEntity entity = repository.findByExternalId(externalId).orElseGet(ConvenioEntity::new);
        entity.setExternalId(externalId);
        apply(entity, request);
        ConvenioEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker(ENTITY_TYPE, saved.getExternalId());
        return toResponse(saved);
    }

    @Transactional
    public void deleteByExternalId(String externalId) {
        ConvenioEntity entity = repository.findByExternalId(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado."));
        deletionLogService.recordDeletion(ENTITY_TYPE, entity.getExternalId());
        repository.delete(entity);
    }

    private ConvenioEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Convênio não encontrado."));
    }

    private void apply(ConvenioEntity entity, ConvenioRequest request) {
        String externalId = request.externalId();
        if ((entity.getExternalId() == null || entity.getExternalId().isBlank()) && externalId != null && !externalId.isBlank()) {
            entity.setExternalId(externalId);
        }
        entity.setNome(request.nome().trim());
        entity.setRegras(request.regras() == null ? List.of() : request.regras().stream().filter(item -> item != null && !item.isBlank()).toList());
        entity.setValorMeiaHora(request.valorMeiaHora());
        entity.setValorBloco(request.valorBloco());
        entity.setPrecisaHora(Boolean.TRUE.equals(request.precisaHora()));
        entity.setAtivo(request.ativo() == null || request.ativo());
        entity.setTipo(request.tipo() == null ? ConvenioTipo.BLOCO : request.tipo());
        entity.replaceFaixas(toFaixas(request.faixas()));
    }

    private List<ConvenioFaixaEntity> toFaixas(List<ConvenioFaixaRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        List<ConvenioFaixaEntity> faixas = new ArrayList<>();
        for (int index = 0; index < requests.size(); index++) {
            ConvenioFaixaRequest request = requests.get(index);
            if (request.max() < request.min()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faixa com max menor que min.");
            }

            ConvenioFaixaEntity faixa = new ConvenioFaixaEntity();
            faixa.setOrdem(index);
            faixa.setMin(request.min());
            faixa.setMax(request.max());
            faixa.setValor(request.valor());
            faixa.setLabel(request.label());
            faixas.add(faixa);
        }
        return faixas;
    }

    private ConvenioResponse toResponse(ConvenioEntity entity) {
        return new ConvenioResponse(
                entity.getId(),
            entity.getExternalId(),
                entity.getNome(),
                entity.getRegras(),
                entity.getValorMeiaHora(),
                entity.getValorBloco(),
                entity.isPrecisaHora(),
                entity.isAtivo(),
                entity.getTipo(),
                entity.getFaixas().stream()
                        .sorted((left, right) -> Integer.compare(left.getOrdem(), right.getOrdem()))
                        .map(this::toFaixaResponse)
                        .toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private ConvenioFaixaResponse toFaixaResponse(ConvenioFaixaEntity entity) {
        return new ConvenioFaixaResponse(
                entity.getId(),
                entity.getOrdem(),
                entity.getMin(),
                entity.getMax(),
                entity.getValor(),
                entity.getLabel()
        );
    }
}