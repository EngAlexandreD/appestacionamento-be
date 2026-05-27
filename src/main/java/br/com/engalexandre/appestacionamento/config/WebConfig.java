package br.com.engalexandre.appestacionamento.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InterceptorAutenticacaoSincronizacao interceptorAutenticacaoSincronizacao;

    public WebConfig(InterceptorAutenticacaoSincronizacao interceptorAutenticacaoSincronizacao) {
        this.interceptorAutenticacaoSincronizacao = interceptorAutenticacaoSincronizacao;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorAutenticacaoSincronizacao)
            .addPathPatterns("/api/sync/**", "/api/v1/**")
                .excludePathPatterns("/api/sync/health");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String userDir = System.getProperty("user.dir");
        final Path base = Paths.get(userDir).toAbsolutePath().normalize();
        final List<String> locations = new ArrayList<>();

        locations.add(_fileLocation(base.resolve("releases")));
        if (base.getParent() != null) {
            locations.add(_fileLocation(base.getParent().resolve("releases")));
        }

        registry.addResourceHandler("/releases/**")
            .addResourceLocations(locations.toArray(new String[0]));
    }

    private static String _fileLocation(Path path) {
        String uri = path.toUri().toString();
        if (!uri.endsWith("/")) {
            uri = uri + "/";
        }
        return uri;
    }
}
