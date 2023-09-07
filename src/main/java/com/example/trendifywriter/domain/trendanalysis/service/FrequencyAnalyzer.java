package com.example.trendifywriter.domain.trendanalysis.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FrequencyAnalyzer {


    public List<Map.Entry<String, Integer>> getTop5FrequentWordsV2(Map<String, Integer> frequencyMap) {
        return frequencyMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
    }




}
