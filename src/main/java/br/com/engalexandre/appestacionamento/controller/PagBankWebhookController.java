package br.com.engalexandre.appestacionamento.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.engalexandre.appestacionamento.service.PixCobrancaService;

// Fora de /api/sync/** e /api/v1/**, de proposito: o PagBank nao tem como
// enviar nosso X-Sync-Token, entao esse endpoint fica fora do interceptor
// (ver WebConfig.addInterceptors). A autenticidade real vem de reconsultar
// a ordem no PagBank (feito dentro de PixCobrancaService), nao do payload
// recebido aqui.
@RestController
@RequestMapping("/api/webhooks/pagbank")
public class PagBankWebhookController {

    private static final Logger log = LoggerFactory.getLogger(PagBankWebhookController.class);

    private final PixCobrancaService service;

    public PagBankWebhookController(PixCobrancaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> receberNotificacao(@RequestBody(required = false) Map<String, Object> payload) {
        String orderId = extrairOrderId(payload);
        if (orderId == null || orderId.isBlank()) {
            log.warn("Webhook do PagBank recebido sem id de ordem identificavel: {}", payload);
            return ResponseEntity.ok().build();
        }

        try {
            service.processarNotificacaoWebhook(orderId);
        } catch (Exception e) {
            // Nunca deixa uma falha aqui gerar retentativa agressiva do PagBank;
            // o proximo webhook (ou o polling do app) tenta de novo naturalmente.
            log.error("Erro ao processar webhook do PagBank para ordem {}", orderId, e);
        }

        return ResponseEntity.ok().build();
    }

    private String extrairOrderId(Map<String, Object> payload) {
        if (payload == null) {
            return null;
        }
        Object id = payload.get("id");
        if (id != null) {
            return id.toString();
        }
        Object order = payload.get("order");
        if (order instanceof Map<?, ?> orderMap) {
            Object orderId = orderMap.get("id");
            if (orderId != null) {
                return orderId.toString();
            }
        }
        return null;
    }
}
