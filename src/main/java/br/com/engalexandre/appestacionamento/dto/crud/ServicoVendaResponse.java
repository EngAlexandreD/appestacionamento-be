package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record ServicoVendaResponse(
        String id,
        String externalId,
        String servicoExternalId,
        String nome,
        String clienteNome,
        String clienteTelefone,
        String placa,
        String modeloVeiculo,
        BigDecimal valor,
        String metodo,
        Instant data,
        Instant createdAt,
        Instant updatedAt
) {
}