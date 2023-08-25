package com.example.trendifywriter.application.service.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeywordExtractor {

    public List<String> extractKeyword(List<String> parsedArticles) {

        List<String> extractedKeywords = new ArrayList<>();

        List<String> filterList = Arrays.asList(
                "통해", "지난", "일부", "혐의", "대한" ,"유튜브","측은","이날","기자","앵커","위반","수사","조사",
                "위해","조사","결과","현재","사실","이번","관련","당시","여부","앞서","다른","정부","경제","대표","가장"); // 필터링할 단어 목록


        for (String article : parsedArticles) {

            String[] words = article.split(" ");

            for (String word : words) {
                if (word.length() > 1 && !filterList.contains(word)) {
                    extractedKeywords.add(word);
                }
            }


        }
        return extractedKeywords;



    }


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