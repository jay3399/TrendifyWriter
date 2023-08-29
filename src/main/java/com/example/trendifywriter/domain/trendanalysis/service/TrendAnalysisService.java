//package com.example.trendifywriter.domain.trendanalysis.service;
//
//import com.example.trendifywriter.application.service.provider.DataProvider;
//import com.example.trendifywriter.domain.trendanalysis.dto.Trend;
//import com.example.trendifywriter.application.service.TrendAnalyzer;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TrendAnalysisService {
//
//    private final DataProvider newsAPIProvider;
//    private final DataProvider socialMediaAPIProvider;
//    private final TrendAnalyzer trendAnalyzer;
//
//    public Trend fetchTrendingData() {
//
//        String s = newsAPIProvider.fetchData();
//        String s1 = socialMediaAPIProvider.fetchData();
//
//        // 결합해 , 처리하는로직
//
//        String analyze = trendAnalyzer.analyze(s + s1);
//
//        Trend trend = new Trend(analyze);
//
//        return trend;
//
//    }
//
//}
