package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record GastoResponse(
        String id,
        String externalId,
        String descricao,
        String categoria,
        BigDecimal valor,
        String observacao,
        Instant dataHora,
        Instant createdAt,
        Instant updatedAt
) {
}
