package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdiantamentoFuncionarioRequest(
        String externalId,
        @NotBlank String funcionarioNome,
        @NotBlank String descricao,
        Instant dataHora,
        @NotNull @DecimalMin("0.0") BigDecimal valor
) {
}
