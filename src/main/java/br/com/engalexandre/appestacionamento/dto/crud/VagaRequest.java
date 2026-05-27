package br.com.engalexandre.appestacionamento.dto.crud;

import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VagaRequest(
        String externalId,
        @NotNull @Min(1) Integer numero,
        Boolean ocupada,
        Boolean mensalista,
        Instant entrada,
        String placa,
        String ticketId,
        String ticketCode,
        String tipoVeiculo,
        String operador
) {
}