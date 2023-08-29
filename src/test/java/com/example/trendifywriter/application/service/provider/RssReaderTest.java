package com.example.trendifywriter.application.service.provider;

import com.example.trendifywriter.application.service.FrequencyTrendAnalyzer;
import com.example.trendifywriter.domain.trendanalysis.service.FrequencyAnalyzer;
import com.example.trendifywriter.domain.trendanalysis.service.KeywordExtractor;
import com.example.trendifywriter.domain.trendanalysis.service.NewsParser;
import com.example.trendifywriter.domain.trendanalysis.service.RssReader;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;


import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import scala.collection.Seq;

class RssReaderTest {



    @Test
    public void reader()  {

        String paragraph = "국민연금에서 김영섭KT대표의 선임안을 찬성하였다";

        CharSequence normalize = OpenKoreanTextProcessorJava.normalize(paragraph);
        System.out.println("normalize = " + normalize);

        Seq<KoreanToken> tokenize = OpenKoreanTextProcessorJava.tokenize(normalize);
//        System.out.println(OpenKoreanTextProcessorJava.tokensToJavaStringList(tokenize));
//        System.out.println(OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokenize));

        List<KoreanPhrase> koreanPhrases = OpenKoreanTextProcessorJava.extractPhrases(tokenize , true , true);

        System.out.println("koreanPhrases = " + koreanPhrases);

        List<String> rsslist = Arrays.asList(
                "https://www.mk.co.kr/rss/30000001/",
                "https://fs.jtbc.co.kr/RSS/newsflash.xml",
                "https://www.hankyung.com/feed/all-news",
                "https://www.hani.co.kr/rss/newsrank/",
                "https://rss.donga.com/total.xml",
                "https://news.sbs.co.kr/news/ReplayRssFeed.do?prog_cd=R1&plink=RSSREADER"

        );

        for (String s : rsslist) {
            RssReader reader = new RssReader();
            List<SyndEntry> articles = reader.fetchArticles(s);
            NewsParser newsParser = new NewsParser();
            List<String> parsed = newsParser.parse(articles);
            KeywordExtractor keywordExtractor = new KeywordExtractor();
            List<String> list = keywordExtractor.extractKeyword(parsed);
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            Map<String, Integer> stringIntegerMap = frequencyAnalyzer.analyzeFrequency(list);
            System.out.println(frequencyAnalyzer.getTop5FrequentWords(stringIntegerMap));

        }

//        RssReader reader = new RssReader();
//
//        List<SyndEntry> articles = reader.fetchArticles("https://www.mk.co.kr/rss/30000001/");
//
//
//        NewsParser newsParser = new NewsParser();
//
//        List<String> parsedArticles = newsParser.parse(articles);
//
//
//        KeywordExtractor keywordExtractor = new KeywordExtractor();
//
//        List<String> keywords = keywordExtractor.extractKeyword(parsedArticles);
//
////        System.out.println("keywords = " + keywords);
//
//        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
//        Map<String, Integer> stringIntegerMap = frequencyAnalyzer.analyzeFrequency(keywords);
//
//        System.out.println("stringIntegerMap = " + stringIntegerMap);

    }

    @Test
    public void all() {

        RssReader rssReader = new RssReader();
        NewsParser newsParser = new NewsParser();
        KeywordExtractor keywordExtractor = new KeywordExtractor();
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();

        FrequencyTrendAnalyzer frequencyTrendAnalyzer = new FrequencyTrendAnalyzer(rssReader,
                newsParser, keywordExtractor, frequencyAnalyzer);

        Map<String, Integer> analyze = frequencyTrendAnalyzer.analyze();

        System.out.println("analyze = " + analyze);


    }

}