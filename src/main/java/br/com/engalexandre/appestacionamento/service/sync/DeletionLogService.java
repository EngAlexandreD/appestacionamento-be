package br.com.engalexandre.appestacionamento.service.sync;

import br.com.engalexandre.appestacionamento.dto.sync.SyncDeletionEntryResponse;
import br.com.engalexandre.appestacionamento.entity.SyncDeletionLogEntity;
import br.com.engalexandre.appestacionamento.repository.SyncDeletionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DeletionLogService {

    private final SyncDeletionLogRepository repository;

    public DeletionLogService(SyncDeletionLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void recordDeletion(String entityType, String externalId) {
        if (entityType == null || entityType.isBlank() || externalId == null || externalId.isBlank()) {
            return;
        }

        SyncDeletionLogEntity entity = repository.findByEntityTypeAndExternalId(entityType, externalId)
                .orElseGet(SyncDeletionLogEntity::new);
        entity.setEntityType(entityType);
        entity.setExternalId(externalId);
        entity.setDeletedAt(Instant.now());
        repository.save(entity);
    }

    @Transactional
    public void clearDeletionMarker(String entityType, String externalId) {
        if (entityType == null || entityType.isBlank() || externalId == null || externalId.isBlank()) {
            return;
        }
        repository.deleteByEntityTypeAndExternalId(entityType, externalId);
    }

    @Transactional(readOnly = true)
    public List<SyncDeletionEntryResponse> listDeletionsSince(Instant cursor) {
        List<SyncDeletionLogEntity> rows = cursor == null
                ? repository.findAllByOrderByDeletedAtAsc()
                : repository.findByDeletedAtAfterOrderByDeletedAtAsc(cursor);

        return rows.stream()
                .map(item -> new SyncDeletionEntryResponse(
                        item.getEntityType(),
                        item.getExternalId(),
                        item.getDeletedAt()
                ))
                .toList();
    }
}