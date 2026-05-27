package br.com.engalexandre.appestacionamento.controller.crud;

import br.com.engalexandre.appestacionamento.dto.crud.VagaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.VagaResponse;
import br.com.engalexandre.appestacionamento.service.crud.VagaCrudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vagas")
public class VagaController {

    private final VagaCrudService service;

    public VagaController(VagaCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<VagaResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public VagaResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VagaResponse create(@Valid @RequestBody VagaRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public VagaResponse update(@PathVariable String id, @Valid @RequestBody VagaRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/external/{externalId}")
    public VagaResponse upsertByExternalId(
            @PathVariable String externalId,
            @Valid @RequestBody VagaRequest request
    ) {
        return service.upsertByExternalId(externalId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/external/{externalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByExternalId(@PathVariable String externalId) {
        service.deleteByExternalId(externalId);
    }
}