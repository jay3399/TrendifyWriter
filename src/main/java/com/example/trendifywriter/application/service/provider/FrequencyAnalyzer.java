package com.example.trendifywriter.application.service.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyAnalyzer {

    public Map<String, Integer> analyzeFrequency(List<String> keywords) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String keyword : keywords) {
            frequencyMap.put(keyword, frequencyMap.getOrDefault(keyword, 0) + 1);

        }

        return frequencyMap;

    }

}
