package br.com.engalexandre.appestacionamento.controller.crud;

import br.com.engalexandre.appestacionamento.dto.crud.ServicoVendaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoVendaResponse;
import br.com.engalexandre.appestacionamento.service.crud.ServicoVendaCrudService;
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
@RequestMapping("/api/v1/servico-vendas")
public class ServicoVendaController {

    private final ServicoVendaCrudService service;

    public ServicoVendaController(ServicoVendaCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServicoVendaResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ServicoVendaResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoVendaResponse create(@Valid @RequestBody ServicoVendaRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ServicoVendaResponse update(@PathVariable String id, @Valid @RequestBody ServicoVendaRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/external/{externalId}")
    public ServicoVendaResponse upsertByExternalId(@PathVariable String externalId, @Valid @RequestBody ServicoVendaRequest request) {
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