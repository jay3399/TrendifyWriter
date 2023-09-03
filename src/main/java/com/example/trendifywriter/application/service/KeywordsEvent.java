package com.example.trendifywriter.application.service;

import java.util.Map;

public class KeywordsEvent {

    private final Map<String, Integer> latestKeywords;

    public KeywordsEvent(Map<String, Integer> latestKeywords) {
        this.latestKeywords = latestKeywords;
    }

    public Map<String, Integer> getLatestKeywords() {
        return latestKeywords;
    }

}
