package br.com.engalexandre.appestacionamento.controller.crud;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.engalexandre.appestacionamento.dto.crud.MovimentacaoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.MovimentacaoResponse;
import br.com.engalexandre.appestacionamento.service.crud.MovimentacaoCrudService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoCrudService service;

    public MovimentacaoController(MovimentacaoCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovimentacaoResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public MovimentacaoResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse create(@Valid @RequestBody MovimentacaoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MovimentacaoResponse update(@PathVariable String id, @Valid @RequestBody MovimentacaoRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/fechar")
    public MovimentacaoResponse markClosed(@PathVariable String id) {
        return service.markClosed(id);
    }

    @PatchMapping("/origem/{operacaoOrigemId}/fechar")
    public MovimentacaoResponse markClosedByOrigin(@PathVariable String operacaoOrigemId) {
        return service.markClosedByOperacaoOrigemId(operacaoOrigemId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @DeleteMapping("/origem/{operacaoOrigemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByOrigin(@PathVariable String operacaoOrigemId) {
        service.deleteByOperacaoOrigemId(operacaoOrigemId);
    }
}