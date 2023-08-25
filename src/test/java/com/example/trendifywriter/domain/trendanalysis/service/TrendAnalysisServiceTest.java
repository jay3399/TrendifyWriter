package com.example.trendifywriter.domain.trendanalysis.service;
import com.example.trendifywriter.application.service.provider.DataProvider;
import com.example.trendifywriter.domain.trendanalysis.analyzer.TrendAnalyzer;
import com.example.trendifywriter.domain.trendanalysis.dto.Trend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
class TrendAnalysisServiceTest {

    private TrendAnalysisService trendAnalysisService;
    private DataProvider newsAPIProvider;
    private DataProvider socialMediaAPIProvider;
    private TrendAnalyzer trendAnalyzer;


    @BeforeEach
    public void setUp() {
        newsAPIProvider = mock(DataProvider.class);
        socialMediaAPIProvider = mock(DataProvider.class);
        trendAnalyzer = mock(TrendAnalyzer.class);
        trendAnalysisService = new TrendAnalysisService(newsAPIProvider, socialMediaAPIProvider, trendAnalyzer);
    }



    @Test
    public void providerTest() {

        when(newsAPIProvider.fetchData()).thenReturn("NewsData");
        when(socialMediaAPIProvider.fetchData()).thenReturn("SocialData");
        when(trendAnalyzer.analyze("NewsDataSocialData")).thenReturn("AnalyzedData");

        // Act
        Trend result = trendAnalysisService.fetchTrendingData();

        // Assert
        assertEquals("AnalyzedData", result.getData());

    }





}