package com.example.trendifywriter.application.service.provider;

import com.rometools.rome.feed.synd.SyndEntry;
import java.util.ArrayList;
import java.util.List;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer.KoreanToken;
import scala.collection.Seq;

public class NewsParser {


    public List<String> parse(List<SyndEntry> rawArticles) {
        List<String> parsedArticles = new ArrayList<>();

        for (SyndEntry article : rawArticles) {

            String title = article.getTitle();


            CharSequence normalize = OpenKoreanTextProcessorJava.normalize(title);

            Seq<KoreanToken> tokenize = OpenKoreanTextProcessorJava.tokenize(normalize);

            List<KoreanPhrase> koreanPhrases = OpenKoreanTextProcessorJava.extractPhrases(tokenize,
                    true, true);

            List<String> phrasesAsStrings = new ArrayList<>();

            for (KoreanPhrase koreanPhrase : koreanPhrases) {
                phrasesAsStrings.add(koreanPhrase.text());
            }
            parsedArticles.add(String.join(" ", phrasesAsStrings));
//            String value = article.getDescription().getValue();

        }
        return parsedArticles;
    }

}
