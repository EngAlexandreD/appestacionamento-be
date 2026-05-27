package br.com.engalexandre.appestacionamento.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record PagamentoMensalistaRequest(
        String externalId,
        Instant dataHora,
        @NotNull @DecimalMin("0.0") BigDecimal valorPago
) {
}