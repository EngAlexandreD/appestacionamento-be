package br.com.engalexandre.appestacionamento.dto.crud;

import java.time.Instant;

public record VagaResponse(
        String id,
        String externalId,
        Integer numero,
        boolean ocupada,
        boolean mensalista,
        Instant entrada,
        String placa,
        String ticketId,
        String ticketCode,
        String tipoVeiculo,
        String operador,
        Instant createdAt,
        Instant updatedAt
) {
}