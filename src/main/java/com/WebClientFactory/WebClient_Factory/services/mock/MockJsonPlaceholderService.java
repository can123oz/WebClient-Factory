package com.WebClientFactory.WebClient_Factory.services.mock;

import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostRequest;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostResponse;
import com.WebClientFactory.WebClient_Factory.services.TestService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "integration.enabled", havingValue = "false")
public class MockJsonPlaceholderService implements TestService {

    @Override
    public AlbumResponse getAlbum(int albumId) {
        return new AlbumResponse(1, albumId, "mock-title");
    }

    @Override
    public PostResponse getPost(int postId) {
        return new PostResponse("mock-title", "mock-body", 1, postId);
    }

    @Override
    public PostResponse addPost(PostRequest post) {
        return new PostResponse(post.title(), post.body(), post.userId(), 101);
    }

}
