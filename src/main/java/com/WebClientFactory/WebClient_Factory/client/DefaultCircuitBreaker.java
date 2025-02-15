package com.WebClientFactory.WebClient_Factory.client;

import com.WebClientFactory.WebClient_Factory.dto.CircuitBreakerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class DefaultCircuitBreaker implements CircuitBreaker {

    @Value("${circuitBreaker.timeoutDuration}")
    private Long timeoutDuration;

    @Value("${circuitBreaker.failureCount}")
    private Integer failureCount;
    private static final Logger logger = LoggerFactory.getLogger(DefaultCircuitBreaker.class);
    private final Map<String, CircuitBreakerState> requestHistory;

    public DefaultCircuitBreaker() {
        this.requestHistory = new HashMap<>();
    }

    @Override
    public boolean isServiceDown(String url) {
        if (!requestHistory.containsKey(url)) {
            return false;
        }

        CircuitBreakerState states = requestHistory.get(url);
        long timeDifference = System.currentTimeMillis() - states.lastFailureTimestamp();

        if (states.serviceDown()) {
            return timeDifference < timeoutDuration;
        }

        return false;
    }

    @Override
    public void successRequest(String url) {
        if (requestHistory.containsKey(url)) {
            requestHistory.remove(url);
            logger.info("Service is back. Url : {}", url);
        }
    }

    @Override
    public void failedRequest(String url) {
        if (requestHistory.containsKey(url)) {
            CircuitBreakerState existingStates = requestHistory.get(url);
            boolean isServiceDown = existingStates.failureCount() >= failureCount;
            requestHistory.put(url, new CircuitBreakerState(isServiceDown, System.currentTimeMillis(), existingStates.failureCount() + 1));
        } else {
            requestHistory.put(url, new CircuitBreakerState(false, System.currentTimeMillis(), 1));
        }
    }
}
