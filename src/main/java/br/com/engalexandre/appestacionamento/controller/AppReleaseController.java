package br.com.engalexandre.appestacionamento.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.dto.RespostaReleaseApp;

@RestController
@RequestMapping("/api/app/releases")
public class AppReleaseController {

    private final String version;
    private final String url;
    private final String notes;
    private final String publishedAt;

    public AppReleaseController(
            @Value("${app.release.version:}") String version,
            @Value("${app.release.url:}") String url,
            @Value("${app.release.notes:}") String notes,
            @Value("${app.release.published-at:}") String publishedAt
    ) {
        this.version = version;
        this.url = url;
        this.notes = notes;
        this.publishedAt = publishedAt;
    }

    @GetMapping("/latest")
    public RespostaReleaseApp latest() {
        if (url == null || url.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Release nao configurada"
            );
        }
        return new RespostaReleaseApp(
                version == null ? "" : version,
                url,
                notes == null ? "" : notes,
                publishedAt == null ? "" : publishedAt
        );
    }
}
