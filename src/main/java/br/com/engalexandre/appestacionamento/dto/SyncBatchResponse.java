package br.com.engalexandre.appestacionamento.dto;

import java.time.Instant;

public record SyncBatchResponse(
        String id,
        String deviceName,
        String snapshotHash,
        boolean duplicated,
        int totalRecords,
        Instant createdAt,
        Instant synchronizedAt
) {
}
