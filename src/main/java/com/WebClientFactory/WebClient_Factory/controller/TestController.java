package com.WebClientFactory.WebClient_Factory.controller;

import com.WebClientFactory.WebClient_Factory.client.WebClient;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostRequest;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private int count = 0;
    private final WebClient<AlbumResponse> albumWebClient;
    private final WebClient<PostResponse> postWebClient;

    public TestController(WebClient<AlbumResponse> albumWebClient,
                          WebClient<PostResponse> postWebClient) {
        this.albumWebClient = albumWebClient;
        this.postWebClient = postWebClient;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        count++;
        return ResponseEntity.ok("pong " + count);
    }

    @GetMapping("/albums-test")
    public ResponseEntity<AlbumResponse> webClientTest(int number) {
        String url = "/albums/" + number;
        return ResponseEntity.ok(albumWebClient.get(url, AlbumResponse.class).block());
    }

    @PostMapping("/posts-test")
    public ResponseEntity<PostResponse> postTest(@RequestBody PostRequest postRequest) {
        PostResponse result = postWebClient.post("/posts", postRequest, PostResponse.class).block();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/posts-test")
    public ResponseEntity<PostResponse> postTest(int number) {
        PostResponse result = postWebClient.get("/posts/" + number, PostResponse.class).block();
        return ResponseEntity.ok(result);
    }
}
