package br.com.engalexandre.appestacionamento.dto;

import java.time.Instant;

// Resposta simples do health check para o cliente decidir se tenta sincronizar.
public record RespostaSaudeServidor(
        String situacao,
        Instant horarioServidor
) {
}