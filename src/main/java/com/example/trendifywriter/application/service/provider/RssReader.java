package com.example.trendifywriter.application.service.provider;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RssReader {

    public List<SyndEntry> fetchArticles(String rssUrl) {
        List<SyndEntry> articles = new ArrayList<>();

        try {
            URL feedUrl = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed build = input.build(new XmlReader(feedUrl));

            articles = build.getEntries();
        } catch (IOException | FeedException exception) {
            exception.printStackTrace();
        }

        return articles;
    }




}


// 매일경제 https://www.mk.co.kr/rss/30000001/


//조선일보  https://www.chosun.com/arc/outboundfeeds/rss/?outputType=xml

//jtbc  https://fs.jtbc.co.kr/RSS/newsflash.xml

//동아일보 https://rss.donga.com/total.xml

//연합뉴스 http://www.yonhapnewstv.co.kr/category/news/headline/feed/

//한국경제 https://www.hankyung.com/feed/all-news
