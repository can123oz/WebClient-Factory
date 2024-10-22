package com.WebClientFactory.WebClient_Factory.controller;

import com.WebClientFactory.WebClient_Factory.client.WebClient;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostRequest;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostResponse;
import com.WebClientFactory.WebClient_Factory.services.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Value("${integration.enabled}")
    private String integrationEnabled;
    private int count = 0;
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        count++;
        return ResponseEntity.ok(integrationEnabled + " pong " + count);
    }

    @GetMapping("/album")
    public ResponseEntity<AlbumResponse> webClientTest(int number) {
        return ResponseEntity.ok(testService.getAlbum(number));
    }

    @PostMapping("/post")
    public ResponseEntity<PostResponse> postTest(@RequestBody PostRequest postRequest) {
        PostResponse result = testService.addPost(postRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/post")
    public ResponseEntity<PostResponse> postTest(int number) {
        PostResponse result = testService.getPost(number);
        return ResponseEntity.ok(result);
    }
}
