package com.example.trendifywriter.domain.blog.repository;

import com.example.trendifywriter.domain.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {


}
