package br.com.engalexandre.appestacionamento.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagBankCustomerRequest(
        String name,
        String email,
        @JsonProperty("tax_id") String taxId
) {
}
