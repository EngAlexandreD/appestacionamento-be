package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.model.SyncBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SyncBatchRepository extends JpaRepository<SyncBatch, String> {

    Optional<SyncBatch> findByDeviceNameAndSnapshotHash(String deviceName, String snapshotHash);

    Optional<SyncBatch> findTopByDeviceNameOrderBySynchronizedAtDesc(String deviceName);

    List<SyncBatch> findBySynchronizedAtBetweenOrderBySynchronizedAtDesc(Instant start, Instant end);
}
