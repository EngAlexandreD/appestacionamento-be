package br.com.engalexandre.appestacionamento.dto.pagbank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// PagBank devolve varios outros campos (links, amount, etc.) que nao usamos;
// ignoreUnknown evita que o parse quebre se a resposta ganhar novos campos.
@JsonIgnoreProperties(ignoreUnknown = true)
public record PagBankQrCodeResponse(
        String id,
        String text,
        @JsonProperty("expiration_date") String expirationDate
) {
}
