package com.WebClientFactory.WebClient_Factory.dto;

public record ApiErrorResponse(String path,
                               String message,
                               String status,
                               Integer statusCode,
                               String stackTrace) {
}
