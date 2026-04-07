package br.com.engalexandre.appestacionamento.controller;

import br.com.engalexandre.appestacionamento.dto.MonthlySyncReportResponse;
import br.com.engalexandre.appestacionamento.dto.ServerHealthResponse;
import br.com.engalexandre.appestacionamento.dto.SyncBatchResponse;
import br.com.engalexandre.appestacionamento.dto.SyncPayloadRequest;
import br.com.engalexandre.appestacionamento.service.SyncService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @GetMapping("/health")
    public ServerHealthResponse health() {
        return new ServerHealthResponse("UP", Instant.now());
    }

    @PostMapping("/batches")
    @ResponseStatus(HttpStatus.CREATED)
    public SyncBatchResponse createBatch(@Valid @RequestBody SyncPayloadRequest request) {
        return syncService.storeSnapshot(request);
    }

    @GetMapping("/batches/latest")
    public SyncBatchResponse latestByDevice(@RequestParam String deviceName) {
        return syncService.getLatestByDevice(deviceName);
    }

    @GetMapping("/reports/monthly")
    public MonthlySyncReportResponse monthlyReport(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return syncService.buildMonthlyReport(year, month);
    }
}
