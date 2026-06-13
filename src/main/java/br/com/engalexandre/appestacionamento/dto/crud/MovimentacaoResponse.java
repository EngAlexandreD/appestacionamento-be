package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record MovimentacaoResponse(
        String id,
        String operacaoOrigemId,
        String ticketId,
        String ticketCode,
        Instant dataHora,
        Instant entrada,
        String convenio,
        String regra,
        BigDecimal valorCliente,
        BigDecimal valorLojista,
        String metodoPagamento,
        String infoAparelho,
        String placa,
        Integer numeroVaga,
        Integer minutosBrutos,
        boolean foiFechado,
        boolean extraviado,
        Instant createdAt,
        Instant updatedAt
) {
}