package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record AdiantamentoFuncionarioResponse(
        String id,
        String externalId,
        String funcionarioNome,
        String descricao,
        Instant dataHora,
        BigDecimal valor,
        Instant createdAt,
        Instant updatedAt
) {
}
