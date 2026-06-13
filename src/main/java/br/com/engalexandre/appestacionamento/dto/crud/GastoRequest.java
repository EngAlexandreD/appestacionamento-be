package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GastoRequest(
        String externalId,
        @NotBlank String descricao,
        @NotBlank String categoria,
        @NotNull @DecimalMin("0.0") BigDecimal valor,
        String observacao,
        Instant dataHora
) {
}
