package com.mishal.socialmedia.service;

import com.mishal.socialmedia.dto.PostRequest;
import com.mishal.socialmedia.entity.Post;
import com.mishal.socialmedia.entity.User;
import com.mishal.socialmedia.exception.UserNotFoundException;
import com.mishal.socialmedia.repository.PostRepository;
import com.mishal.socialmedia.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(String username, PostRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found: " + username));

        Post post = new Post();
        post.setContent(request.getContent());
        post.setUser(user);

        return postRepository.save(post);
    }

    public Page<Post> getPostsByUser(String username,int page,int size) {
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("id").descending());

        return postRepository.findByUserUsername(username,pageRequest);
    }
}
