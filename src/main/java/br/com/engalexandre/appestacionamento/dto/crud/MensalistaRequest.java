package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MensalistaRequest(
        String externalId,
        @NotBlank String nome,
        String placa,
        String telefone,
        @NotNull @DecimalMin("0.0") BigDecimal valorMensalidade,
        @NotNull @Min(1) @Max(31) Integer diaVencimento,
        Boolean ativo
) {
}