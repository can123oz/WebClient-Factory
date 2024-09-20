package com.WebClientFactory.WebClient_Factory.dto;

public record CircuitBreakerState(boolean serviceDown, long lastFailureTimestamp, int failureCount) {
}
