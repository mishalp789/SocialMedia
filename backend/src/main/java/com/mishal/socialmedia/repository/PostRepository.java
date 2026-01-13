package com.mishal.socialmedia.repository;

import com.mishal.socialmedia.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserUsername(String username, Pageable pageable);
}
