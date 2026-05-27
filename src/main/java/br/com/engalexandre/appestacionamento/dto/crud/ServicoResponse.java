package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record ServicoResponse(
        String id,
        String externalId,
        String nome,
        BigDecimal preco,
        Integer cor,
        Integer ordem,
        boolean ativo,
        Instant createdAt,
        Instant updatedAt
) {
}