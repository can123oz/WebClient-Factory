package com.WebClientFactory.WebClient_Factory.client;

import reactor.core.publisher.Mono;

public interface WebClient<T> {
    Mono<T> get(String url, Class<T> responseType);
    Mono<T> post(String url, Object body, Class<T> responseType);
}
