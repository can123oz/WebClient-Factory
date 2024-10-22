package com.WebClientFactory.WebClient_Factory.services;

import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.AlbumResponse;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostRequest;
import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostResponse;

public interface TestService {
    AlbumResponse getAlbum(int albumId);
    PostResponse getPost(int postId);
    PostResponse addPost(PostRequest post);
}
