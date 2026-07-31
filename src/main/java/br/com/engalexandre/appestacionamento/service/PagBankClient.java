package br.com.engalexandre.appestacionamento.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import br.com.engalexandre.appestacionamento.config.PropriedadesPagBank;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankAmount;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankCustomerRequest;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankOrderRequest;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankOrderResponse;
import br.com.engalexandre.appestacionamento.dto.pagbank.PagBankQrCodeRequest;

// Fala com a API do PagBank (criar ordem PIX / consultar status). Nunca
// confia cegamente no corpo do webhook: quem decide o status pago e sempre
// uma chamada de volta a consultarOrdem, autenticada com nosso proprio token.
@Service
public class PagBankClient {

    private static final Logger log = LoggerFactory.getLogger(PagBankClient.class);

    // O PagBank exige um "customer" mesmo em cobrancas avulsas via QR Code,
    // mas o app nao coleta dados do cliente (venda anonima de estacionamento/
    // PDV) — usamos um cliente generico fixo so para satisfazer a API.
    private static final PagBankCustomerRequest CLIENTE_GENERICO = new PagBankCustomerRequest(
            "Cliente Estacionamento",
            "cliente@estacionamento.local",
            "12345678909"
    );

    private final RestClient restClient;
    private final PropriedadesPagBank propriedades;

    public PagBankClient(RestClient pagBankRestClient, PropriedadesPagBank propriedades) {
        this.restClient = pagBankRestClient;
        this.propriedades = propriedades;
    }

    public PagBankOrderResponse criarOrdemPix(BigDecimal valor, String referenciaOrigemId, Instant expiraEm) {
        exigirConfigurado();

        int valorCentavos = valor.setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValueExact();

        List<String> notificationUrls = (propriedades.getWebhookUrl() == null || propriedades.getWebhookUrl().isBlank())
                ? List.of()
                : List.of(propriedades.getWebhookUrl());

        PagBankOrderRequest requestBody = new PagBankOrderRequest(
                referenciaOrigemId,
                CLIENTE_GENERICO,
                List.of(new PagBankQrCodeRequest(new PagBankAmount(valorCentavos), expiraEm.toString())),
                notificationUrls
        );

        try {
            return restClient.post()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + propriedades.getToken())
                    .body(requestBody)
                    .retrieve()
                    .body(PagBankOrderResponse.class);
        } catch (RestClientException e) {
            logErroPagBank("criar ordem PIX", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao criar cobranca PIX no PagBank.", e);
        }
    }

    public PagBankOrderResponse consultarOrdem(String pagbankOrderId) {
        exigirConfigurado();
        try {
            return restClient.get()
                    .uri("/orders/{id}", pagbankOrderId)
                    .header("Authorization", "Bearer " + propriedades.getToken())
                    .retrieve()
                    .body(PagBankOrderResponse.class);
        } catch (RestClientException e) {
            logErroPagBank("consultar ordem PIX", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao consultar cobranca PIX no PagBank.", e);
        }
    }

    // O corpo do erro do PagBank (ex.: "must not be null") nao aparecia em
    // lugar nenhum antes — sem isso, cada erro de integracao vira uma
    // investigacao manual via curl direto na API deles.
    private void logErroPagBank(String operacao, RestClientException e) {
        if (e instanceof RestClientResponseException responseException) {
            log.error("Erro do PagBank ao {}: HTTP {} - {}", operacao,
                    responseException.getStatusCode(), responseException.getResponseBodyAsString());
        } else {
            log.error("Erro de comunicacao com o PagBank ao {}", operacao, e);
        }
    }

    private void exigirConfigurado() {
        if (!propriedades.isConfigurado()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "PagBank nao configurado no servidor.");
        }
    }
}
