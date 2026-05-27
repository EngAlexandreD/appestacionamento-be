package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record ProdutoVendaResponse(
        String id,
        String externalId,
        String produtoExternalId,
        String nome,
        Integer quantidade,
        BigDecimal valorVenda,
        Instant data,
        Instant createdAt,
        Instant updatedAt
) {
}