package com.example.trendifywriter.domain.trendanalysis.service;

import com.rometools.rome.feed.synd.SyndEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import org.springframework.stereotype.Component;
import scala.collection.Seq;

@Component
public class NewsParser {


    // -> 병렬스트림 성능 +
    public List<String> parse(List<SyndEntry> rawArticles) {

        ConcurrentLinkedQueue<String> collect = rawArticles.parallelStream()
                .flatMap(article -> {
                    String content = article.getTitle() + article.getDescription();
                    CharSequence normalizedContent = OpenKoreanTextProcessorJava.normalize(content);
                    Seq<KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(
                            normalizedContent);
                    List<KoreanPhrase> phrases = OpenKoreanTextProcessorJava.extractPhrases(tokens,
                            true, true);
                    return phrases.stream()
                            .map(KoreanPhrase::text)
                            .filter(text -> !text.matches(".*\\d+.*"))
                            .filter(text -> !text.matches(".*[a-zA-Z]+.*"))
                            .filter(text -> !text.matches("\\d+일"))
                            .filter(text -> !text.matches(".+(구|시|마을|군)$"));
                })
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));

        return new ArrayList<>(collect);
    }
}
