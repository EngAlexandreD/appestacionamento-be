package br.com.engalexandre.appestacionamento.dto.sync;

import java.time.Instant;

public record SyncDeletionEntryResponse(
        String entityType,
        String externalId,
        Instant deletedAt
) {
}