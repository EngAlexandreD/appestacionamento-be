package br.com.engalexandre.appestacionamento.dto;

// Informacoes da ultima release disponivel para download do app.
public record RespostaReleaseApp(
        String version,
        String url,
        String notes,
        String publishedAt
) {
}
