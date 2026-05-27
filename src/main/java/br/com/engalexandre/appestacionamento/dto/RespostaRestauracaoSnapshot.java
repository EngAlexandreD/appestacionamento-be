package br.com.engalexandre.appestacionamento.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

// Resposta especifica para restauracao do ultimo snapshot salvo no servidor.
public record RespostaRestauracaoSnapshot(
        String id,
        String nomeDispositivo,
        String hashSnapshot,
        String versaoApp,
        Instant criadoEm,
        Instant sincronizadoEm,
        int totalRegistros,
        JsonNode dadosSnapshot
) {
}