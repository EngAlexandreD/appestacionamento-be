package br.com.engalexandre.appestacionamento.dto;

import java.time.Instant;
import java.util.List;

public record MonthlySyncReportResponse(
        int year,
        int month,
        long totalBatches,
        long totalDevices,
        long totalRecords,
        Instant lastSynchronizationAt,
        List<DeviceSyncSummary> devices
) {
    public record DeviceSyncSummary(
            String deviceName,
            long batches,
            long totalRecords,
            Instant lastSynchronizationAt
    ) {
    }
}
