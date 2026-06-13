package br.com.engalexandre.appestacionamento.controller.crud;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.engalexandre.appestacionamento.dto.crud.GastoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.GastoResponse;
import br.com.engalexandre.appestacionamento.service.crud.GastoCrudService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/gastos")
public class GastoController {

    private final GastoCrudService service;

    public GastoController(GastoCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<GastoResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public GastoResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GastoResponse upsert(@Valid @RequestBody GastoRequest request) {
        return service.upsert(request);
    }

    @DeleteMapping("/external/{externalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByExternalId(@PathVariable String externalId) {
        service.delete(externalId);
    }
}
