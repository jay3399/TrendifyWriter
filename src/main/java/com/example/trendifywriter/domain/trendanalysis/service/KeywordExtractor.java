package com.example.trendifywriter.domain.trendanalysis.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class KeywordExtractor {


    // List -> HashSet
    public List<String> extractKeyword(List<String> parsedArticles) {

        ConcurrentLinkedQueue<String> collect = parsedArticles.parallelStream()
                .flatMap(article -> Arrays.stream(article.split(" ")))
                .filter(word -> word.length() > 1 && !FilterList.shouldFilter(word))
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));

        return new ArrayList<>(collect);

    }

//        List<String> extractedKeywords = new ArrayList<>();
//
//        for (String article : parsedArticles) {
//            String[] words = article.split(" ");
//            for (String word : words) {
//                if (word.length() > 1 && !filterList.contains(word)) {
//                    extractedKeywords.add(word);
//                }
//            }
//        }
//        return extractedKeywords;
//    }


}


//    List<String> extractedKeywords = new ArrayList<>();
//
//        for (String article : parsedArticles) {
//
//
//
//                String[] words = article.split(" ");
//                extractedKeywords.addAll(Arrays.asList(words));
//
//                }
//
//                return extractedKeywords;