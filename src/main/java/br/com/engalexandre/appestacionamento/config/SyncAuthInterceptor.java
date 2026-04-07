package br.com.engalexandre.appestacionamento.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SyncAuthInterceptor implements HandlerInterceptor {

    private final SyncAuthProperties properties;

    public SyncAuthInterceptor(SyncAuthProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String expectedToken = properties.getToken();
        if (expectedToken == null || expectedToken.isBlank()) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Token de sincronizacao nao configurado no servidor.");
            return false;
        }

        String headerName = properties.getHeaderName();
        String providedToken = request.getHeader(headerName);

        if (!expectedToken.equals(providedToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de sincronizacao invalido.");
            return false;
        }

        return true;
    }
}