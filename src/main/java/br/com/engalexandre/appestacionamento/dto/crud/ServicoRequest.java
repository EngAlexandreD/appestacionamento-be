package br.com.engalexandre.appestacionamento.dto.crud;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServicoRequest(
        String externalId,
        @NotBlank String nome,
        @NotNull @DecimalMin("0.0") BigDecimal preco,
        Integer cor,
        Integer ordem,
        Boolean ativo
) {
}