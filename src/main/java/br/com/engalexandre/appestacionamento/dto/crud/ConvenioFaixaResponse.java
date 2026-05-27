package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;

public record ConvenioFaixaResponse(
        String id,
        Integer ordem,
        Integer min,
        Integer max,
        BigDecimal valor,
        String label
) {
}