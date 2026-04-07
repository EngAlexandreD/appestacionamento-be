package br.com.engalexandre.appestacionamento.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.engalexandre.appestacionamento.dto.MonthlySyncReportResponse;
import br.com.engalexandre.appestacionamento.dto.SyncBatchResponse;
import br.com.engalexandre.appestacionamento.dto.SyncPayloadRequest;
import br.com.engalexandre.appestacionamento.model.SyncBatch;
import br.com.engalexandre.appestacionamento.repository.SyncBatchRepository;

@Service
public class SyncService {

    private final SyncBatchRepository repository;
    private final ObjectMapper objectMapper;

    public SyncService(SyncBatchRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public SyncBatchResponse storeSnapshot(SyncPayloadRequest request) {
        return repository.findByDeviceNameAndSnapshotHash(request.getDeviceName(), request.getSnapshotHash())
                .map(existing -> toResponse(existing, true))
                .orElseGet(() -> persistNew(request));
    }

    public SyncBatchResponse getLatestByDevice(String deviceName) {
        SyncBatch batch = repository.findTopByDeviceNameOrderBySynchronizedAtDesc(deviceName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma sincronizacao encontrada para o dispositivo."));
        return toResponse(batch, false);
    }

    public MonthlySyncReportResponse buildMonthlyReport(int year, int month) {
        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mes deve estar entre 1 e 12.");
        }

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Instant start = LocalDateTime.of(year, month, 1, 0, 0).atZone(zoneId).toInstant();
        Instant end = LocalDateTime.of(year, month, 1, 0, 0).plusMonths(1).atZone(zoneId).toInstant();

        List<SyncBatch> batches = repository.findBySynchronizedAtBetweenOrderBySynchronizedAtDesc(start, end);
        Map<String, List<SyncBatch>> byDevice = batches.stream()
                .collect(Collectors.groupingBy(SyncBatch::getDeviceName));

        List<MonthlySyncReportResponse.DeviceSyncSummary> devices = byDevice.entrySet().stream()
                .map(entry -> {
                    long totalRecords = entry.getValue().stream().mapToLong(SyncBatch::getTotalRecords).sum();
                    Instant lastSync = entry.getValue().stream()
                            .map(SyncBatch::getSynchronizedAt)
                            .max(Comparator.naturalOrder())
                            .orElse(null);
                    return new MonthlySyncReportResponse.DeviceSyncSummary(
                            entry.getKey(),
                            entry.getValue().size(),
                            totalRecords,
                            lastSync
                    );
                })
                .sorted(Comparator.comparing(MonthlySyncReportResponse.DeviceSyncSummary::deviceName))
                .toList();

        Instant lastSync = batches.stream()
                .map(SyncBatch::getSynchronizedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        long totalRecords = batches.stream().mapToLong(SyncBatch::getTotalRecords).sum();

        return new MonthlySyncReportResponse(
                year,
                month,
                batches.size(),
                devices.size(),
                totalRecords,
                lastSync,
                devices
        );
    }

    private SyncBatchResponse persistNew(SyncPayloadRequest request) {
        SyncBatch batch = new SyncBatch();
        batch.setDeviceName(request.getDeviceName());
        batch.setSnapshotHash(request.getSnapshotHash());
        batch.setAppVersion(request.getAppVersion());
        batch.setCreatedAt(request.getCreatedAt() == null ? Instant.now() : request.getCreatedAt());
        batch.setSynchronizedAt(Instant.now());
        batch.setTotalRecords(calculateTotalRecords(request));
        batch.setPayloadJson(serializePayload(request));

        SyncBatch saved = repository.save(batch);
        return toResponse(saved, false);
    }

    private int calculateTotalRecords(SyncPayloadRequest request) {
        return sizeOf(request.getVagas())
                + sizeOf(request.getConvenios())
                + sizeOf(request.getMovimentacoes())
                + sizeOf(request.getProdutos())
                + sizeOf(request.getVendas())
                + sizeOf(request.getServicos())
            + sizeOf(request.getMensalistas())
            + sizeOf(request.getRawState());
    }

    private int sizeOf(JsonNode node) {
        if (node == null || node.isNull()) {
            return 0;
        }
        if (node.isArray()) {
            return node.size();
        }
        if (node.isObject()) {
            return node.size();
        }
        return 1;
    }

    private String serializePayload(SyncPayloadRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nao foi possivel serializar o payload recebido.");
        }
    }

    private SyncBatchResponse toResponse(SyncBatch batch, boolean duplicated) {
        return new SyncBatchResponse(
                batch.getId(),
                batch.getDeviceName(),
                batch.getSnapshotHash(),
                duplicated,
                batch.getTotalRecords(),
                batch.getCreatedAt(),
                batch.getSynchronizedAt()
        );
    }
}
