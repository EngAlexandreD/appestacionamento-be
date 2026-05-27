package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record ProdutoResponse(
        String id,
        String externalId,
        String nome,
        Integer quantidade,
        BigDecimal valorPago,
        BigDecimal margemLucro,
        BigDecimal valorVenda,
        String codigoBarras,
        Instant createdAt,
        Instant updatedAt
) {
}