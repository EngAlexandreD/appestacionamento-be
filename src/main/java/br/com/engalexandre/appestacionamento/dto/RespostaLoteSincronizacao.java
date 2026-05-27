package br.com.engalexandre.appestacionamento.dto;

import java.time.Instant;

// Resposta padrao devolvida pelos endpoints de gravacao e consulta de lotes.
public record RespostaLoteSincronizacao(
        String id,
        String nomeDispositivo,
        String hashSnapshot,
        boolean duplicado,
        int totalRegistros,
        Instant criadoEm,
        Instant sincronizadoEm
) {
}