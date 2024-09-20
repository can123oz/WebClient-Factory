package com.WebClientFactory.WebClient_Factory.controller;

import com.WebClientFactory.WebClient_Factory.client.WebClient;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Value("${provider.baseUrl}")
    private String baseUrl;
    private int count = 0;
    private final WebClient<AlbumResponse> albumWebClient;

    public TestController(WebClient<AlbumResponse> albumWebClient) {
        this.albumWebClient = albumWebClient;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        count++;
        return ResponseEntity.ok("pong " + count);
    }

    @GetMapping("/webclient-test")
    public ResponseEntity<AlbumResponse> webClientTest(int number) {
        String url = baseUrl + "/albums/" + number;
        return ResponseEntity.ok(albumWebClient.get(url, AlbumResponse.class).block());
    }
}
