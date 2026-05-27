package br.com.engalexandre.appestacionamento.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.sync.auth")
public class PropriedadesAutenticacaoSincronizacao {

    // Mantem o nome do header configuravel sem alterar codigo.
    private String headerName = "X-Sync-Token";

    // Token compartilhado entre app e servidor para proteger os endpoints de sync.
    private String token = "";

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}