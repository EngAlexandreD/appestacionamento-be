package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequest(
        String operacaoOrigemId,
        @NotBlank String ticketId,
        String ticketCode,
        Instant dataHora,
        Instant entrada,
        @NotBlank String convenio,
        @NotBlank String regra,
        @NotNull @DecimalMin("0.0") BigDecimal valorCliente,
        @NotNull @DecimalMin("0.0") BigDecimal valorLojista,
        @NotBlank String metodoPagamento,
        @NotBlank String infoAparelho,
        String placa,
        Integer numeroVaga,
        Integer minutosBrutos,
        Boolean foiFechado
) {
}