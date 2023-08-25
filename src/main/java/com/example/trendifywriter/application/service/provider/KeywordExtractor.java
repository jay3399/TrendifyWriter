package com.example.trendifywriter.application.service.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeywordExtractor {

    public List<String> extractKeyword(List<String> parsedArticles) {
        List<String> extractedKeywords = new ArrayList<>();

        for (String article : parsedArticles) {
            String[] words = article.split(" ");
            extractedKeywords.addAll(Arrays.asList(words));

        }

        return extractedKeywords;

    }


}
