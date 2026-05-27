package br.com.engalexandre.appestacionamento.controller.crud;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRecebimentoRequest;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRecebimentoResponse;
import br.com.engalexandre.appestacionamento.service.crud.ConvenioRecebimentoCrudService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/convenios")
public class ConvenioRecebimentoController {

    private final ConvenioRecebimentoCrudService service;

    public ConvenioRecebimentoController(ConvenioRecebimentoCrudService service) {
        this.service = service;
    }

    @PostMapping("/{convenioExternalId}/recebimentos")
    @ResponseStatus(HttpStatus.CREATED)
    public ConvenioRecebimentoResponse registerRecebimento(
            @PathVariable String convenioExternalId,
            @Valid @RequestBody ConvenioRecebimentoRequest request
    ) {
        return service.registerRecebimento(convenioExternalId, request);
    }

    @PutMapping("/external/{convenioExternalId}/recebimentos/{recebimentoExternalId}")
    public ConvenioRecebimentoResponse upsertRecebimento(
            @PathVariable String convenioExternalId,
            @PathVariable String recebimentoExternalId,
            @Valid @RequestBody ConvenioRecebimentoRequest request
    ) {
        return service.upsertByExternalIds(convenioExternalId, recebimentoExternalId, request);
    }

    @PostMapping("/external/{convenioExternalId}/recebimentos/{recebimentoExternalId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ConvenioRecebimentoResponse postUpsertRecebimento(
            @PathVariable String convenioExternalId,
            @PathVariable String recebimentoExternalId,
            @Valid @RequestBody ConvenioRecebimentoRequest request
    ) {
        return service.upsertByExternalIds(convenioExternalId, recebimentoExternalId, request);
    }

    @DeleteMapping("/recebimentos/{externalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecebimento(@PathVariable String externalId) {
        service.deleteByExternalId(externalId);
    }
}
