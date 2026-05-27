package br.com.engalexandre.appestacionamento.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ConvenioFaixaRequest(
        @NotNull @Min(0) Integer min,
        @NotNull @Min(0) Integer max,
        @NotNull @DecimalMin("0.0") BigDecimal valor,
        String label
) {
}