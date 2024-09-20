package com.WebClientFactory.WebClient_Factory.exception;

public class BaseUrlNullException extends RuntimeException {
    public BaseUrlNullException() {
        super("baseUrl of the service cannot be null");
    }
}
