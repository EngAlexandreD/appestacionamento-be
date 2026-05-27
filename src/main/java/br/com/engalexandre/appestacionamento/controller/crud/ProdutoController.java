package br.com.engalexandre.appestacionamento.controller.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ProdutoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoResponse;
import br.com.engalexandre.appestacionamento.service.crud.ProdutoCrudService;
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
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoCrudService service;

    public ProdutoController(ProdutoCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProdutoResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ProdutoResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse create(@Valid @RequestBody ProdutoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ProdutoResponse update(@PathVariable String id, @Valid @RequestBody ProdutoRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/external/{externalId}")
    public ProdutoResponse upsertByExternalId(@PathVariable String externalId, @Valid @RequestBody ProdutoRequest request) {
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