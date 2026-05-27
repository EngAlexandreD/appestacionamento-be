package br.com.engalexandre.appestacionamento.dto.crud;

import br.com.engalexandre.appestacionamento.entity.ConvenioTipo;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ConvenioResponse(
        String id,
        String externalId,
        String nome,
        List<String> regras,
        BigDecimal valorMeiaHora,
        BigDecimal valorBloco,
        boolean precisaHora,
        boolean ativo,
        ConvenioTipo tipo,
        List<ConvenioFaixaResponse> faixas,
        Instant createdAt,
        Instant updatedAt
) {
}