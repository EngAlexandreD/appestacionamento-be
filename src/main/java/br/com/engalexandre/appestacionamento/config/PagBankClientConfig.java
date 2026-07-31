package br.com.engalexandre.appestacionamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PagBankClientConfig {

    @Bean
    public RestClient pagBankRestClient(PropriedadesPagBank propriedades) {
        return RestClient.builder()
                .baseUrl(propriedades.getApiUrl())
                .build();
    }
}
