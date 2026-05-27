package br.com.engalexandre.appestacionamento.controller.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ProdutoVendaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoVendaResponse;
import br.com.engalexandre.appestacionamento.service.crud.ProdutoVendaCrudService;
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
@RequestMapping("/api/v1/produto-vendas")
public class ProdutoVendaController {

    private final ProdutoVendaCrudService service;

    public ProdutoVendaController(ProdutoVendaCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProdutoVendaResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ProdutoVendaResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoVendaResponse create(@Valid @RequestBody ProdutoVendaRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ProdutoVendaResponse update(@PathVariable String id, @Valid @RequestBody ProdutoVendaRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/external/{externalId}")
    public ProdutoVendaResponse upsertByExternalId(@PathVariable String externalId, @Valid @RequestBody ProdutoVendaRequest request) {
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