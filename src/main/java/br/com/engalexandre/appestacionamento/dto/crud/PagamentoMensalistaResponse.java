package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record PagamentoMensalistaResponse(
        String id,
        String externalId,
        Instant dataHora,
        BigDecimal valorPago,
        Instant createdAt,
        Instant updatedAt
) {
}