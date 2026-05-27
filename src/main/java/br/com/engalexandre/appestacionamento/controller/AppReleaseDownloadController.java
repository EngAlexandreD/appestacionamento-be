package br.com.engalexandre.appestacionamento.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AppReleaseDownloadController {

    private static final Set<String> ALLOWED_RELEASE_FILES = Set.of(
        "appestacionamento.apk",
        "appestacionamento-arm64-v8a.apk",
        "appestacionamento-armeabi-v7a.apk",
        "appestacionamento-x86_64.apk"
    );

    @GetMapping(value = "/releases/{fileName:.+\\.apk}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> download(@PathVariable String fileName) {
        if (!ALLOWED_RELEASE_FILES.contains(fileName)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Release nao encontrada");
        }

        Path file = resolveReleaseFile(fileName);
        if (file == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Release nao encontrada");
        }

        Resource resource = toResource(file);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    private Path resolveReleaseFile(String fileName) {
        Path base = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path first = base.resolve("releases").resolve(fileName);
        if (java.nio.file.Files.exists(first)) {
            return first;
        }
        if (base.getParent() != null) {
            Path second = base.getParent().resolve("releases").resolve(fileName);
            if (java.nio.file.Files.exists(second)) {
                return second;
            }
        }
        return null;
    }

    private Resource toResource(Path path) {
        try {
            return new UrlResource(path.toUri());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao ler release");
        }
    }
}
