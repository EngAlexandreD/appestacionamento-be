package br.com.engalexandre.appestacionamento.controller;

import br.com.engalexandre.appestacionamento.dto.sync.IncrementalSyncPullResponse;
import br.com.engalexandre.appestacionamento.service.sync.IncrementalSyncPullService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/sync")
public class SyncPullController {

    private final IncrementalSyncPullService service;

    public SyncPullController(IncrementalSyncPullService service) {
        this.service = service;
    }

    @GetMapping("/pull")
    public IncrementalSyncPullResponse pull(
            @RequestParam(name = "cursor", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant cursor
    ) {
        return service.pullChanges(cursor);
    }
}