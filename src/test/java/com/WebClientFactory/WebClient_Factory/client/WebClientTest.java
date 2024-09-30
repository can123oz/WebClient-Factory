package com.WebClientFactory.WebClient_Factory.client;

import com.WebClientFactory.WebClient_Factory.exception.BaseUrlNullException;
import com.WebClientFactory.WebClient_Factory.exception.CircuitBreakerException;
import com.WebClientFactory.WebClient_Factory.exception.ClientException;
import com.WebClientFactory.WebClient_Factory.utils.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WebClientTest {

    @Mock
    private CircuitBreaker circuitBreaker;

    private DefaultWebClient<String> webClient;
    private MockWebServer mockWebServer;
    private String fakeUrl = "test-url";


    @BeforeEach
    public void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        webClient = new DefaultWebClient(circuitBreaker, fakeUrl);
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        // Sets the mock WebClient to our webClient
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        TestUtil.setPrivateField(this.webClient, "webClient", webClient);

    }

    @Test
    void shouldInitThrowsException() throws NoSuchFieldException, IllegalAccessException {
        //Given
        //When
        TestUtil.setPrivateField(webClient, "baseUrl", null);

        //Then
        assertThrows(BaseUrlNullException.class, () -> webClient.init());
    }

    @Test
    void shouldInitCallsWebClient() throws NoSuchFieldException, IllegalAccessException {
        //Given
        TestUtil.setPrivateField(webClient, "baseUrl", "test-url");

        //When
        webClient.init();
        // Use reflection to access the private field 'webClient'
        Field webClientField = DefaultWebClient.class.getDeclaredField("webClient");
        webClientField.setAccessible(true);  // Allow access to private field
        WebClient webClient = (WebClient) webClientField.get(this.webClient);

        //Then
        assertNotNull(webClient);
    }

    @Test
    void shouldGetMethodThrowException() {
        //Given
        String url = "test-url-in-timeout";
        
        //When
        given(circuitBreaker.isServiceDown(url)).willReturn(true);
        
        //Then
        assertThrows(CircuitBreakerException.class,
                () -> webClient.get(url, String.class).block());
    }

    @Test
    void shouldGetMethodReturnsMonoSuccess() {
        //Given
        String url = "test-url";
        String mockResponse = "Mock response body";

        //When
        given(circuitBreaker.isServiceDown(url)).willReturn(false);

        // Simulate a successful 200 response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<String> responseMono = webClient.get(url, String.class);

        //Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.equals(mockResponse))
                .verifyComplete();

        verify(circuitBreaker, times(1)).isServiceDown(anyString());
        verify(circuitBreaker, times(1)).successRequest(anyString());
    }

    @Test
    void shouldGetMethodReturnsMono4XXError() {
        //Given
        String url = "test-url";

        //When
        given(circuitBreaker.isServiceDown(url)).willReturn(false);

        // Simulate a successful 200 response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400));

        Mono<String> responseMono = webClient.get(url, String.class);

        //Then
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> {
                    if (!(throwable instanceof ClientException)) return false;
                    String errorMessage = throwable.getMessage();
                    return errorMessage.contains("Client error");
                })
                .verify();

        verify(circuitBreaker, times(1)).isServiceDown(anyString());
    }

    @Test
    void shouldGetMethodReturnsMono5XXError() {
        //Given
        String url = "test-url";

        //When
        given(circuitBreaker.isServiceDown(url)).willReturn(false);

        // Simulate a successful 200 response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        Mono<String> responseMono = webClient.get(url, String.class);

        //Then
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> {
                    if (!(throwable instanceof CircuitBreakerException)) return false;
                    String errorMessage = throwable.getMessage();
                    return errorMessage.contains("Server error");
                })
                .verify();

        verify(circuitBreaker, times(1)).isServiceDown(anyString());
    }

    @Test
    void shouldPostMethodReturnsMono5XXError() {
        //Given
        String url = "test-url";

        //When
        given(circuitBreaker.isServiceDown(url)).willReturn(false);

        // Simulate a successful 200 response
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        Mono<String> responseMono = webClient.post(url, Object.class, String.class);

        //Then
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable -> {
                    if (!(throwable instanceof CircuitBreakerException)) return false;
                    String errorMessage = throwable.getMessage();
                    return errorMessage.contains("Server error");
                })
                .verify();

        verify(circuitBreaker, times(1)).isServiceDown(anyString());
    }

}

