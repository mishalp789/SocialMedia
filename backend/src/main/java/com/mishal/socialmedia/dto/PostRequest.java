package com.mishal.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;

public class PostRequest {

    @NotBlank(message = "Post content cannot be empty")
    private String content;

    // getter & setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
