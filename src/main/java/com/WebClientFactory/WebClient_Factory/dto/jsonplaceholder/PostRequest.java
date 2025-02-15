package com.WebClientFactory.WebClient_Factory.dto.jsonplaceholder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        @NotBlank(message = "Title is mandatory") @NotNull String title,
        String body,
        @Min(1) Integer userId) {
}
