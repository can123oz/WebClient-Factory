package com.WebClientFactory.WebClient_Factory.client;

import com.WebClientFactory.WebClient_Factory.exception.BaseUrlNullException;
import com.WebClientFactory.WebClient_Factory.exception.CircuitBreakerException;
import com.WebClientFactory.WebClient_Factory.exception.ClientException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.Map;

public class DefaultWebClient<Response> implements WebClient<Response> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultWebClient.class);
    private org.springframework.web.reactive.function.client.WebClient webClient;
    private final CircuitBreaker circuitBreaker;
    private final String baseUrl;

    @Value("${provider.token}")
    private String token;

    public DefaultWebClient(CircuitBreaker circuitBreaker,
                            String baseUrl) {
        this.circuitBreaker = circuitBreaker;
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    public void init() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new BaseUrlNullException();
        }
        webClient = getWebClient();
    }

    @Override
    public Mono<Response> get(String url, Class<Response> responseType) {
        Mono<Response> error = getResponseMono(url);
        if (error != null) return error;

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ClientException("Client error : " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    circuitBreaker.failedRequest(url);
                    return Mono.error(new CircuitBreakerException("Server error : " + clientResponse.statusCode()));
                })
                .bodyToMono(responseType)
                .doOnSuccess(response -> circuitBreaker.successRequest(url));
    }

    @Override
    public Mono<Response> post(String url, Object body, Class<Response> responseType) {
        Mono<Response> error = getResponseMono(url);
        if (error != null) return error;

        return webClient.post()
                .uri(url)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ClientException("Client error : " + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    circuitBreaker.failedRequest(url);
                    return Mono.error(new CircuitBreakerException("Server error : " + clientResponse.statusCode()));
                })
                .bodyToMono(responseType)
                .doOnSuccess(response -> circuitBreaker.successRequest(url));
    }

    private Mono<Response> getResponseMono(String url) {
        if (circuitBreaker.isServiceDown(url)) {
            logger.error("Service is down no need to send more request at the moment. Url : {}", url);
            return Mono.error(new CircuitBreakerException("Service is down no need to send more request at the moment."));
        }
        return null;
    }

    private org.springframework.web.reactive.function.client.WebClient getWebClient() {
        return org.springframework.web.reactive.function.client.WebClient
                .builder()
                .baseUrl(baseUrl)
                .filter(logFilter())
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    private ExchangeFilterFunction logFilter() {
        return (clientRequest, next) -> {
            logger.info("External Request to {}", clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction responseFilter() {
        return (clientRequest, next) -> {
            logger.info("External Response body : {}", clientRequest.body());
            return next.exchange(clientRequest);
        };
    }
}
