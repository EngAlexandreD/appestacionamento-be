package br.com.engalexandre.appestacionamento.service.crud;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.crud.MovimentacaoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.MovimentacaoResponse;
import br.com.engalexandre.appestacionamento.entity.MovimentacaoEntity;
import br.com.engalexandre.appestacionamento.repository.MovimentacaoRepository;
import br.com.engalexandre.appestacionamento.service.sync.DeletionLogService;

@Service
public class MovimentacaoCrudService {

    private final MovimentacaoRepository repository;
    private final DeletionLogService deletionLogService;

    public MovimentacaoCrudService(
            MovimentacaoRepository repository,
            DeletionLogService deletionLogService
    ) {
        this.repository = repository;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listAll() {
        return repository.findAllByOrderByDataHoraDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public MovimentacaoResponse create(MovimentacaoRequest request) {
        if (request.operacaoOrigemId() != null && !request.operacaoOrigemId().isBlank()) {
            return repository.findByOperacaoOrigemId(request.operacaoOrigemId())
                    .map(existing -> {
                        apply(existing, request);
                        MovimentacaoEntity saved = repository.save(existing);
                        deletionLogService.clearDeletionMarker("movimentacao", resolveSyncId(saved));
                        return toResponse(saved);
                    })
                    .orElseGet(() -> persistNew(request));
        }

        return persistNew(request);
    }

    private MovimentacaoResponse persistNew(MovimentacaoRequest request) {
        MovimentacaoEntity entity = new MovimentacaoEntity();
        apply(entity, request);
        MovimentacaoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker("movimentacao", resolveSyncId(saved));
        return toResponse(saved);
    }

    @Transactional
    public MovimentacaoResponse update(String id, MovimentacaoRequest request) {
        MovimentacaoEntity entity = getEntity(id);
        apply(entity, request);
        MovimentacaoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker("movimentacao", resolveSyncId(saved));
        return toResponse(saved);
    }

    @Transactional
    public MovimentacaoResponse markClosed(String id) {
        MovimentacaoEntity entity = getEntity(id);
        entity.setFoiFechado(true);
        MovimentacaoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker("movimentacao", resolveSyncId(saved));
        return toResponse(saved);
    }

    @Transactional
    public MovimentacaoResponse markClosedByOperacaoOrigemId(String operacaoOrigemId) {
        MovimentacaoEntity entity = repository.findByOperacaoOrigemId(operacaoOrigemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimentação não encontrada."));
        entity.setFoiFechado(true);
        MovimentacaoEntity saved = repository.save(entity);
        deletionLogService.clearDeletionMarker("movimentacao", resolveSyncId(saved));
        return toResponse(saved);
    }

    @Transactional
    public void delete(String id) {
        MovimentacaoEntity entity = getEntity(id);
        repository.delete(entity);
        deletionLogService.recordDeletion("movimentacao", resolveSyncId(entity));
    }

    @Transactional
    public void deleteByOperacaoOrigemId(String operacaoOrigemId) {
        MovimentacaoEntity entity = repository.findByOperacaoOrigemId(operacaoOrigemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimentação não encontrada."));
        repository.delete(entity);
        deletionLogService.recordDeletion("movimentacao", resolveSyncId(entity));
    }

    private String resolveSyncId(MovimentacaoEntity entity) {
        if (entity.getOperacaoOrigemId() != null && !entity.getOperacaoOrigemId().isBlank()) {
            return entity.getOperacaoOrigemId();
        }
        return entity.getId();
    }

    private MovimentacaoEntity getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimentação não encontrada."));
    }

    private void apply(MovimentacaoEntity entity, MovimentacaoRequest request) {
        entity.setOperacaoOrigemId(request.operacaoOrigemId());
        entity.setTicketId(request.ticketId().trim());
        entity.setTicketCode(request.ticketCode());
        entity.setDataHora(request.dataHora() == null ? Instant.now() : request.dataHora());
        entity.setEntrada(request.entrada());
        entity.setConvenio(request.convenio().trim());
        entity.setRegra(request.regra().trim());
        entity.setValorCliente(request.valorCliente());
        entity.setValorLojista(request.valorLojista());
        entity.setMetodoPagamento(request.metodoPagamento().trim());
        entity.setInfoAparelho(request.infoAparelho().trim());
        entity.setPlaca(request.placa());
        entity.setNumeroVaga(request.numeroVaga());
        entity.setMinutosBrutos(request.minutosBrutos());
        entity.setFoiFechado(Boolean.TRUE.equals(request.foiFechado()));
    }

    private MovimentacaoResponse toResponse(MovimentacaoEntity entity) {
        return new MovimentacaoResponse(
                entity.getId(),
                entity.getOperacaoOrigemId(),
                entity.getTicketId(),
                entity.getTicketCode(),
                entity.getDataHora(),
                entity.getEntrada(),
                entity.getConvenio(),
                entity.getRegra(),
                entity.getValorCliente(),
                entity.getValorLojista(),
                entity.getMetodoPagamento(),
                entity.getInfoAparelho(),
                entity.getPlaca(),
                entity.getNumeroVaga(),
                entity.getMinutosBrutos(),
                entity.isFoiFechado(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}