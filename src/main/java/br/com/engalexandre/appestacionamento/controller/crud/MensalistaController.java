package br.com.engalexandre.appestacionamento.controller.crud;

import br.com.engalexandre.appestacionamento.dto.crud.MensalistaRequest;
import br.com.engalexandre.appestacionamento.dto.crud.MensalistaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.PagamentoMensalistaRequest;
import br.com.engalexandre.appestacionamento.service.crud.MensalistaCrudService;
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
@RequestMapping("/api/v1/mensalistas")
public class MensalistaController {

    private final MensalistaCrudService service;

    public MensalistaController(MensalistaCrudService service) {
        this.service = service;
    }

    @GetMapping
    public List<MensalistaResponse> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public MensalistaResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MensalistaResponse create(@Valid @RequestBody MensalistaRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MensalistaResponse update(@PathVariable String id, @Valid @RequestBody MensalistaRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/external/{externalId}")
    public MensalistaResponse upsertByExternalId(
            @PathVariable String externalId,
            @Valid @RequestBody MensalistaRequest request
    ) {
        return service.upsertByExternalId(externalId, request);
    }

    @PostMapping("/{id}/pagamentos")
    @ResponseStatus(HttpStatus.CREATED)
    public MensalistaResponse registerPayment(@PathVariable String id, @Valid @RequestBody PagamentoMensalistaRequest request) {
        return service.registerPayment(id, request);
    }

    @PutMapping("/external/{externalId}/pagamentos/{paymentExternalId}")
    public MensalistaResponse upsertPaymentByExternalIds(
            @PathVariable String externalId,
            @PathVariable String paymentExternalId,
            @Valid @RequestBody PagamentoMensalistaRequest request
    ) {
        return service.upsertPaymentByExternalIds(externalId, paymentExternalId, request);
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