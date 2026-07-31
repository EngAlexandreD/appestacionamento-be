package br.com.engalexandre.appestacionamento.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagBankQrCodeRequest(
        PagBankAmount amount,
        @JsonProperty("expiration_date") String expirationDate
) {
}
