package com.example.trendifywriter.domain.post.repository;

import com.example.trendifywriter.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {


}
