package com.mishal.socialmedia.controller;

import com.mishal.socialmedia.dto.PostRequest;
import com.mishal.socialmedia.entity.Post;
import com.mishal.socialmedia.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{username}/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@PathVariable String username,
                           @Valid @RequestBody PostRequest request) {
        return postService.createPost(username, request);
    }


    @GetMapping
    public Page<Post> getUserPosts(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return postService.getPostsByUser(username,page,size);
    }
}
