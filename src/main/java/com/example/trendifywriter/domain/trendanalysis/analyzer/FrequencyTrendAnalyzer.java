package com.example.trendifywriter.domain.trendanalysis.analyzer;

import org.springframework.stereotype.Component;

@Component
public class FrequencyTrendAnalyzer implements TrendAnalyzer {

    @Override
    public String analyze(String data) {
        return "결과";
    }
}
