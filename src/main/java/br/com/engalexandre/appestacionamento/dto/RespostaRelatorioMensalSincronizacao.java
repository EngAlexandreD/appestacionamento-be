package br.com.engalexandre.appestacionamento.dto;

import java.time.Instant;
import java.util.List;

// Estrutura agregada do relatorio mensal para consulta administrativa.
public record RespostaRelatorioMensalSincronizacao(
        int ano,
        int mes,
        long totalLotes,
        long totalDispositivos,
        long totalRegistros,
        Instant ultimaSincronizacaoEm,
        List<ResumoSincronizacaoDispositivo> dispositivos
) {
    // Resumo por dispositivo para manter o payload do relatorio objetivo.
    public record ResumoSincronizacaoDispositivo(
            String nomeDispositivo,
            long lotes,
            long totalRegistros,
            Instant ultimaSincronizacaoEm
    ) {
    }
}