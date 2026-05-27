package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record MensalistaResponse(
        String id,
        String externalId,
        String nome,
        String placa,
        String telefone,
        BigDecimal valorMensalidade,
        Integer diaVencimento,
        boolean ativo,
        List<PagamentoMensalistaResponse> pagamentos,
        Instant createdAt,
        Instant updatedAt
) {
}