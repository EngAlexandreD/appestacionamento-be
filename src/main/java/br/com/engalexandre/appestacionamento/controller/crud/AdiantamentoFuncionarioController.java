package br.com.engalexandre.appestacionamento.controller.crud;

import java.util.List;

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

import br.com.engalexandre.appestacionamento.dto.crud.AdiantamentoFuncionarioRequest;
import br.com.engalexandre.appestacionamento.dto.crud.AdiantamentoFuncionarioResponse;
import br.com.engalexandre.appestacionamento.service.crud.AdiantamentoFuncionarioCrudService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/adiantamentos")
public class AdiantamentoFuncionarioController {

    private final AdiantamentoFuncionarioCrudService service;

    public AdiantamentoFuncionarioController(AdiantamentoFuncionarioCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdiantamentoFuncionarioResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public AdiantamentoFuncionarioResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdiantamentoFuncionarioResponse create(@Valid @RequestBody AdiantamentoFuncionarioRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AdiantamentoFuncionarioResponse update(
            @PathVariable String id,
            @Valid @RequestBody AdiantamentoFuncionarioRequest request
    ) {
        return service.update(id, request);
    }

    @PutMapping("/external/{externalId}")
    public AdiantamentoFuncionarioResponse upsertByExternalId(
            @PathVariable String externalId,
            @Valid @RequestBody AdiantamentoFuncionarioRequest request
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
