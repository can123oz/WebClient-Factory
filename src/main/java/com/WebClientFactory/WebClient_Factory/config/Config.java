package com.WebClientFactory.WebClient_Factory.config;

import com.WebClientFactory.WebClient_Factory.client.CircuitBreaker;
import com.WebClientFactory.WebClient_Factory.client.DefaultCircuitBreaker;
import com.WebClientFactory.WebClient_Factory.client.DefaultWebClient;
import com.WebClientFactory.WebClient_Factory.client.WebClient;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public CircuitBreaker circuitBreaker() {
        return new DefaultCircuitBreaker();
    }

    @Bean
    public WebClient<AlbumResponse> albumWebClient(CircuitBreaker circuitBreaker) {
        return new DefaultWebClient<>(circuitBreaker);
    }

    @Bean
    public OpenAPI OpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Web Client Factory")
                        .description("By Can Ozdemir")
                        .version("1.0"));
    }
}
