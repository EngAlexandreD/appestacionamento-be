package br.com.engalexandre.appestacionamento.controller.crud;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.engalexandre.appestacionamento.dto.crud.PixCobrancaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.PixCobrancaResponse;
import br.com.engalexandre.appestacionamento.service.PixCobrancaService;
import jakarta.validation.Valid;

// Sob /api/v1/**, entao ja herda o interceptor de X-Sync-Token (WebConfig).
@RestController
@RequestMapping("/api/v1/pix/cobrancas")
public class PixCobrancaController {

    private final PixCobrancaService service;

    public PixCobrancaController(PixCobrancaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PixCobrancaResponse criar(@Valid @RequestBody PixCobrancaRequest request) {
        return service.criar(request);
    }

    @GetMapping("/{id}")
    public PixCobrancaResponse consultar(@PathVariable String id) {
        return service.consultarStatusAtual(id);
    }
}
