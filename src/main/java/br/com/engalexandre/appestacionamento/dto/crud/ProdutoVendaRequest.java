package br.com.engalexandre.appestacionamento.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record ProdutoVendaRequest(
        String externalId,
        String produtoExternalId,
        @NotBlank String nome,
        @NotNull @Min(1) Integer quantidade,
        @NotNull @DecimalMin("0.0") BigDecimal valorVenda,
        Instant data
) {
}