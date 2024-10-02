package com.WebClientFactory.WebClient_Factory.client;

import com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder.PostRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebClientIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetPostTestReturnAPost() throws Exception {
        int postId = 1;
        String titleResponse = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit";
        ResultActions result = mockMvc.perform(get("/api/v1/test/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("number", String.valueOf(postId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(titleResponse));

        result.andExpect(status().isOk());
        result.andDo(print());
    }

    @Test
    void shouldPostTestReturnAPost() throws Exception {
        int userId = 1;
        String title = "test title";
        String body = "test body new inserted.";

        PostRequest postRequest = new PostRequest(title, body, userId);

        ResultActions result = mockMvc.perform(post("/api/v1/test/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(postRequest))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.id").value(101));

        result.andExpect(status().isOk());
        result.andDo(print());
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail(e);
            return null;
        }
    }
}
