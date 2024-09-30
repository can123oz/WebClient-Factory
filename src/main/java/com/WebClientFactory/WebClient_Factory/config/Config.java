package com.WebClientFactory.WebClient_Factory.config;

import com.WebClientFactory.WebClient_Factory.client.CircuitBreaker;
import com.WebClientFactory.WebClient_Factory.client.DefaultCircuitBreaker;
import com.WebClientFactory.WebClient_Factory.client.DefaultWebClient;
import com.WebClientFactory.WebClient_Factory.client.WebClient;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${provider.baseUrl}")
    private String baseUrl;

    @Bean
    public CircuitBreaker circuitBreaker() {
        return new DefaultCircuitBreaker();
    }

    @Bean
    public WebClient<AlbumResponse> albumWebClient(CircuitBreaker circuitBreaker) {
        return new DefaultWebClient<>(circuitBreaker, baseUrl);
    }

    @Bean
    public WebClient<PostResponse> postWebClient(CircuitBreaker circuitBreaker) {
        return new DefaultWebClient<>(circuitBreaker, baseUrl);
    }

    @Bean
    public OpenAPI OpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Web Client Factory")
                        .description("By Can Ozdemir")
                        .version("1.0"));
    }
}
