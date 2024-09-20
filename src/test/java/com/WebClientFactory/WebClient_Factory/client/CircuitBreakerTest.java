package com.WebClientFactory.WebClient_Factory.client;

import com.WebClientFactory.WebClient_Factory.dto.CircuitBreakerState;
import com.WebClientFactory.WebClient_Factory.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircuitBreakerTest {

    private DefaultCircuitBreaker circuitBreaker;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        circuitBreaker = new DefaultCircuitBreaker();

        TestUtil.setPrivateField(circuitBreaker, "timeoutDuration", 10000L);
        TestUtil.setPrivateField(circuitBreaker, "failureCount", 3);
    }

    @Test
    void shouldIsServiceDownReturnsFalse() {
        //Given
        String url = "test-url";

        //When
        boolean isServiceDown = circuitBreaker.isServiceDown(url);

        //Then
        assertFalse(isServiceDown);
    }

    @Test
    void shouldIsServiceDownReturnsTrue() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory", Map.of(
                url, new CircuitBreakerState(true, System.currentTimeMillis(), 1)
        ));

        boolean isServiceDown = circuitBreaker.isServiceDown(url);

        //Then
        assertTrue(isServiceDown);
    }

    @Test
    void shouldIsServiceDownReturnsFalseButUrkContains() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory", Map.of(
                url, new CircuitBreakerState(false, System.currentTimeMillis(), 1)
        ));

        boolean isServiceDown = circuitBreaker.isServiceDown(url);

        //Then
        assertFalse(isServiceDown);
    }

    @Test
    void shouldIsServiceDownTimeDifferenceBiggerThanDurationReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory", Map.of(
                url, new CircuitBreakerState(true, 100L, 1)
        ));

        boolean isServiceDown = circuitBreaker.isServiceDown(url);

        //Then
        assertFalse(isServiceDown);
    }

    @Test
    void shouldSuccessRequestNotContainsUrl() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url-for-success";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory",
                new HashMap<>(Map.of(url, new CircuitBreakerState(false, 100L, 1))));
        circuitBreaker.successRequest("Some-Other-Url-Not-Exist");

        //Then
        assertFalse(circuitBreaker.isServiceDown(url));
    }

    @Test
    void shouldSuccessRequestContainsUrlAndDeleteIt() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url-for-success";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory",
                new HashMap<>(Map.of(url, new CircuitBreakerState(false, 100L, 1))));
        circuitBreaker.successRequest(url);

        //Then
        assertFalse(circuitBreaker.isServiceDown(url));
    }

    @Test
    void shouldFailedRequestContainsUrlAndPassesFailureCount() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url-for-failed";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory",
                new HashMap<>(Map.of(url, new CircuitBreakerState(true, 100L, 10))));
        circuitBreaker.failedRequest(url);

        //Then
        assertTrue(circuitBreaker.isServiceDown(url));
    }

    @Test
    void shouldFailedRequestContainsUrlButNotPassingFailureCount() throws NoSuchFieldException, IllegalAccessException {
        //Given
        String url = "test-url-for-failed";

        //When
        TestUtil.setPrivateField(circuitBreaker, "requestHistory",
                new HashMap<>(Map.of(url, new CircuitBreakerState(true, 100L, 1))));
        circuitBreaker.failedRequest(url);

        //Then
        assertFalse(circuitBreaker.isServiceDown(url));
    }

    @Test
    void shouldFailedRequestNotContainsUrl() {
        //Given
        String url = "test-url-for-not-contains";

        //When
        circuitBreaker.failedRequest(url);
        //Then
        assertFalse(circuitBreaker.isServiceDown(url));
    }


}
