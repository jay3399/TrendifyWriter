package com.example.trendifywriter.application.service.provider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FrequencyAnalyzer {

    public Map<String, Integer> analyzeFrequency(List<String> keywords) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String keyword : keywords) {
            frequencyMap.put(keyword, frequencyMap.getOrDefault(keyword, 0) + 1);

        }

        return frequencyMap;

    }

    public Map<String, Integer> getTop5FrequentWords(Map<String, Integer> frequencyMap) {
        return frequencyMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
