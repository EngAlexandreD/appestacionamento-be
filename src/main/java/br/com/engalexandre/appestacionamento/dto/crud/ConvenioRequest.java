package br.com.engalexandre.appestacionamento.dto.crud;

import br.com.engalexandre.appestacionamento.entity.ConvenioTipo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ConvenioRequest(
        String externalId,
        @NotBlank String nome,
        List<String> regras,
        @NotNull @DecimalMin("0.0") BigDecimal valorMeiaHora,
        @NotNull @DecimalMin("0.0") BigDecimal valorBloco,
        Boolean precisaHora,
        Boolean cobraCiclos,
        Boolean ativo,
        ConvenioTipo tipo,
        @Valid List<ConvenioFaixaRequest> faixas
) {
}