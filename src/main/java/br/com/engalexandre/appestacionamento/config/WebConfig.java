package br.com.engalexandre.appestacionamento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SyncAuthInterceptor syncAuthInterceptor;

    public WebConfig(SyncAuthInterceptor syncAuthInterceptor) {
        this.syncAuthInterceptor = syncAuthInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(syncAuthInterceptor)
                .addPathPatterns("/api/sync/**")
                .excludePathPatterns("/api/sync/health");
    }
}
