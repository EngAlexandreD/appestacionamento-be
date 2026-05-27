package br.com.engalexandre.appestacionamento.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class InterceptorAutenticacaoSincronizacao implements HandlerInterceptor {

    private final PropriedadesAutenticacaoSincronizacao propriedades;

    public InterceptorAutenticacaoSincronizacao(PropriedadesAutenticacaoSincronizacao propriedades) {
        this.propriedades = propriedades;
    }

    // Valida o token compartilhado antes de permitir acesso aos endpoints protegidos.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String tokenEsperado = propriedades.getToken();
        if (tokenEsperado == null || tokenEsperado.isBlank()) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Token de sincronizacao nao configurado no servidor.");
            return false;
        }

        String nomeCabecalho = propriedades.getHeaderName();
        String tokenInformado = request.getHeader(nomeCabecalho);

        if (!tokenEsperado.equals(tokenInformado)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de sincronizacao invalido.");
            return false;
        }

        return true;
    }
}