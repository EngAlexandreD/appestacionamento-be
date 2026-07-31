package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PixCobrancaRequest(
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotBlank String referenciaOrigemId,
        @NotBlank String origemTipo
) {
}
