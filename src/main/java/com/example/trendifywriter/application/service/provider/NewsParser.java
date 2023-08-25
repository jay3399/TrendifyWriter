package com.example.trendifywriter.application.service.provider;

import com.rometools.rome.feed.synd.SyndEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import scala.collection.Seq;

public class NewsParser {


    public List<String> parse(List<SyndEntry> rawArticles) {
        List<String> parsedArticles = new ArrayList<>();



        for (SyndEntry article : rawArticles) {

            String content = article.getTitle() + article.getDescription();


            CharSequence normalize = OpenKoreanTextProcessorJava.normalize(content);

            Seq<KoreanToken> tokenize = OpenKoreanTextProcessorJava.tokenize(normalize);

            List<KoreanPhrase> koreanPhrases = OpenKoreanTextProcessorJava.extractPhrases(tokenize,
                    true, true);

            List<String> filteredPhrases = koreanPhrases.stream()
                    .map(KoreanPhrase::text)
                    .filter(text -> !text.matches(".*\\d+.*"))  // 숫자 포함 단어 제외
                    .filter(text -> !text.matches(".*[a-zA-Z]+.*"))  // 영어 포함 단어 제외
                    .filter(text -> !text.matches("\\d+일"))  // 숫자 + '일' 단어 제외
                    .filter(text -> !text.matches(".+(구|시|마을|군)$"))
                    .collect(Collectors.toList());


            List<String> phrasesAsStrings = new ArrayList<>();
//
            for (KoreanPhrase koreanPhrase : koreanPhrases) {
                phrasesAsStrings.add(koreanPhrase.text());
            }
//            parsedArticles.add(String.join(" ", phrasesAsStrings));
//            String value = article.getDescription().getValue();
            parsedArticles.addAll(filteredPhrases);

        }
        return parsedArticles;
    }

}
