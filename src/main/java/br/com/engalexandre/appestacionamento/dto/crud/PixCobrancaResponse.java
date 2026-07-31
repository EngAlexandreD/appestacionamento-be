package br.com.engalexandre.appestacionamento.dto.crud;

import java.math.BigDecimal;
import java.time.Instant;

public record PixCobrancaResponse(
        String id,
        String status,
        BigDecimal valor,
        String qrCodeTexto,
        Instant expiraEm,
        Instant pagoEm
) {
}
