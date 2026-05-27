package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record ConvenioRecebimentoResponse(
        String id,
        String externalId,
        String convenioExternalId,
        String convenioNome,
        Instant dataHora,
        BigDecimal valorPago,
        String metodoPagamento,
        String observacao,
        Instant createdAt,
        Instant updatedAt
) {
}
