package br.com.engalexandre.appestacionamento.dto;

import java.time.Instant;

public record ServerHealthResponse(
        String status,
        Instant serverTime
) {
}
