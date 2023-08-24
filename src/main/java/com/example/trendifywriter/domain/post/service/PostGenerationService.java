package com.example.trendifywriter.domain.post.service;

import com.example.trendifywriter.domain.post.model.Post;
import com.example.trendifywriter.domain.trendanalysis.dto.Trend;
import com.example.trendifywriter.domain.trendanalysis.service.TrendAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostGenerationService {

    private final TrendAnalysisService trendAnalysisService;


    public Post generatePostFromTrends(Trend trend) {

        // trend + gpt 이용 글 생성 메서드

        Post post = new Post();

        return post;
    }

}
