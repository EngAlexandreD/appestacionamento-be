package br.com.engalexandre.appestacionamento.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record ServicoVendaRequest(
        String externalId,
        String servicoExternalId,
        @NotBlank String nome,
        @NotNull @DecimalMin("0.0") BigDecimal valor,
        @NotBlank String metodo,
        Instant data
) {
}