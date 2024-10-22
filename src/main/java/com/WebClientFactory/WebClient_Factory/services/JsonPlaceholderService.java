package com.WebClientFactory.WebClient_Factory.services;

import com.WebClientFactory.WebClient_Factory.client.WebClient;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostRequest;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "integration.enabled")
public class JsonPlaceholderService implements TestService {

    private final WebClient<AlbumResponse> albumWebClient;
    private final WebClient<PostResponse> postWebClient;

    public JsonPlaceholderService(WebClient<AlbumResponse> albumWebClient,
                                  WebClient<PostResponse> postWebClient) {
        this.albumWebClient = albumWebClient;
        this.postWebClient = postWebClient;
    }

    @Override
    public AlbumResponse getAlbum(int albumId) {
        String url = "/albums/" + albumId;
        return albumWebClient.get(url, AlbumResponse.class).block();
    }

    @Override
    public PostResponse getPost(int postId) {
        return postWebClient.get("/posts/" + postId, PostResponse.class).block();
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        return postWebClient.post("/posts", post, PostResponse.class).block();
    }
}
