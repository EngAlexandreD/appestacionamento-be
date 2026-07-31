package br.com.engalexandre.appestacionamento.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PagBankChargeResponse(
        String id,
        String status
) {
}
