//package com.example.trendifywriter.application.service;
//
//
//import com.example.trendifywriter.domain.blog.service.BlogService;
//import com.example.trendifywriter.domain.post.model.Post;
//import com.example.trendifywriter.domain.post.service.PostGenerationService;
//import com.example.trendifywriter.domain.trendanalysis.dto.Trend;
//import com.example.trendifywriter.domain.trendanalysis.service.TrendAnalysisService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class PostGenerationApplicationService {
//
//    private final PostGenerationService postGenerationService;
//
//    private final TrendAnalysisService trendAnalysisService;
//
//    private final BlogService blogService;
//
//
//
//    // 트렌트 가져와서 , 분석 및 분석데이터로 POST 생성후 , 블로그에 업로드
//
//    public void generateAndPublishPost() {
//
//        Trend trend = trendAnalysisService.fetchTrendingData();
//
//        Post post = postGenerationService.generatePostFromTrends(trend);
//
//        blogService.publishPost(post);
//
//    }
//
//
//
//}
