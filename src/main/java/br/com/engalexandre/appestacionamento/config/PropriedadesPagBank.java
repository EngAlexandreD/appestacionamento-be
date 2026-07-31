package br.com.engalexandre.appestacionamento.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.pagbank")
public class PropriedadesPagBank {

    // Sandbox por padrao; troque para https://api.pagseguro.com em producao.
    private String apiUrl = "https://sandbox.api.pagseguro.com";

    // Token vazio = integracao desligada (PagBankClient falha rapido com 503,
    // que e o mesmo sinal que o app usa para cair no QR estatico manual).
    private String token = "";

    // URL publica (Tailscale Funnel) que o PagBank deve chamar ao confirmar o pagamento.
    private String webhookUrl = "";

    private int expiracaoSegundos = 300;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public int getExpiracaoSegundos() {
        return expiracaoSegundos;
    }

    public void setExpiracaoSegundos(int expiracaoSegundos) {
        this.expiracaoSegundos = expiracaoSegundos;
    }

    public boolean isConfigurado() {
        return token != null && !token.isBlank();
    }
}
