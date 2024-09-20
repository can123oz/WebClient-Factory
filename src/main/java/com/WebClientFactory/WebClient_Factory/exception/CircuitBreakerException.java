package com.WebClientFactory.WebClient_Factory.exception;

public class CircuitBreakerException extends RuntimeException {
    public CircuitBreakerException(String message) {
        super(message);
    }
}
