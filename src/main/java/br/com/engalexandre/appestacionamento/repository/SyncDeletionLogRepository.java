package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.SyncDeletionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SyncDeletionLogRepository extends JpaRepository<SyncDeletionLogEntity, String> {

    Optional<SyncDeletionLogEntity> findByEntityTypeAndExternalId(String entityType, String externalId);

    List<SyncDeletionLogEntity> findByDeletedAtAfterOrderByDeletedAtAsc(Instant deletedAt);

    List<SyncDeletionLogEntity> findAllByOrderByDeletedAtAsc();

    void deleteByEntityTypeAndExternalId(String entityType, String externalId);
}