package com.WebClientFactory.WebClient_Factory.client;

public interface CircuitBreaker {
    boolean isServiceDown(String url);
    void failedRequest(String url);
    void successRequest(String url);
}
