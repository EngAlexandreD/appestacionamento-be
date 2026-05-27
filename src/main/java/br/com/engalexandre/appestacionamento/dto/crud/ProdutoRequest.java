package br.com.engalexandre.appestacionamento.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequest(
        String externalId,
        @NotBlank String nome,
        @NotNull @Min(0) Integer quantidade,
        @NotNull @DecimalMin("0.0") BigDecimal valorPago,
        @NotNull @DecimalMin("0.0") BigDecimal margemLucro,
        @NotNull @DecimalMin("0.0") BigDecimal valorVenda,
        String codigoBarras
) {
}