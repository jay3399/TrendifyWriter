package com.example.trendifywriter.application.service;

import com.example.trendifywriter.domain.trendanalysis.service.FrequencyAnalyzer;
import com.example.trendifywriter.domain.trendanalysis.service.KeywordExtractor;
import com.example.trendifywriter.domain.trendanalysis.service.NewsParser;
import com.example.trendifywriter.domain.trendanalysis.service.RssReader;
import com.rometools.rome.feed.synd.SyndEntry;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.net.http.WebSocket;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FrequencyTrendAnalyzer implements TrendAnalyzer {

    private final RssReader reader;

    private final NewsParser parser;

    private final KeywordExtractor keywordExtractor;

    private final FrequencyAnalyzer frequencyAnalyzer;


    private final List<String> rsslist = Arrays.asList(
            "https://www.mk.co.kr/rss/30000001/",
            "https://fs.jtbc.co.kr/RSS/newsrank.xml",
            "https://fs.jtbc.co.kr/RSS/newsroom.xml",
            "https://www.hani.co.kr/rss/newsrank/",
            "https://www.hani.co.kr/rss/lead/",
            "https://www.hani.co.kr/rss/",
            "https://rss.donga.com/total.xml",
            "https://news.sbs.co.kr/news/ReplayRssFeed.do?prog_cd=R1&plink=RSSREADER"
    );


    @Override
    public Map<String , Integer> analyze() {

        Map<String, Integer> aggregatedMap = new ConcurrentHashMap<>();

        rsslist.parallelStream().forEach(s -> {
            List<SyndEntry> articles = reader.fetchArticles(s);
            List<String> parsedArticles = parser.parse(articles);
            List<String> keywords = keywordExtractor.extractKeyword(parsedArticles);

            Map<String, Integer> frequencyMap = keywords.stream()
                    .collect(Collectors.groupingBy(Function.identity(),
                            Collectors.summingInt(e -> 1)));

            List<Map.Entry<String, Integer>> top5 = frequencyAnalyzer.getTop5FrequentWordsV2(
                    frequencyMap);

            top5.forEach(
                    entry -> aggregatedMap.merge(entry.getKey(), entry.getValue(), Integer::sum));

//            redisTemplate.opsForHash().putAll("realtime_keywords", aggregatedMap);
        });

        System.out.println("aggregatedMap = " + aggregatedMap);

        return aggregatedMap;
//        return aggregatedMap.entrySet()
//                .stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue())
//                .limit(10)
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));
    }
}
//        Map<String, Integer> aggregatedMap = new HashMap<>();
//
//        for (String s : rsslist) {
//            List<SyndEntry> articles = reader.fetchArticles(s);
//            List<String> parsedArticles = parser.parse(articles);
//            List<String> keywords = keywordExtractor.extractKeyword(parsedArticles);
//
//            Map<String, Integer> frequencyMap = keywords.stream()
//                    .collect(Collectors.groupingBy(keyword -> keyword, Collectors.summingInt(keyword -> 1)));
//
//            List<Map.Entry<String, Integer>> top5 = frequencyAnalyzer.getTop5FrequentWordsV2(frequencyMap);
//
//            top5.forEach(entry -> aggregatedMap.put(entry.getKey(), aggregatedMap.getOrDefault(entry.getKey(), 0) + entry.getValue()));
//        }

//    @Override
//    public Map<String, Integer> analyze() {
//
//        Map<String, Integer> stringIntegerMap = new HashMap<>();
//
//        for (String s : rsslist) {
//            List<SyndEntry> articles = reader.fetchArticles(s);
//            List<String> parse = parser.parse(articles);
//            List<String> strings = keywordExtractor.extractKeyword(parse);
//
//
//            Map<String, Integer> analyzeFrequency = frequencyAnalyzer.analyzeFrequency(strings);
//
//            Map<String, Integer> top5FrequentWords = frequencyAnalyzer.getTop5FrequentWords(analyzeFrequency);
//
//
//            for (Entry<String, Integer> entry : top5FrequentWords.entrySet()) {
//                stringIntegerMap.put(entry.getKey(),
//                        stringIntegerMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
//            }
//        }
//
//        return stringIntegerMap;
//    }
