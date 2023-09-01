package com.example.trendifywriter.domain.scheduletask;

import java.util.Map;

public class KeywordsUpdatedEvent {

    private final Map<String, Integer> latestKeywords;

    public KeywordsUpdatedEvent(Map<String, Integer> latestKeywords) {
        this.latestKeywords = latestKeywords;
    }

    public Map<String, Integer> getLatestKeywords() {
        return latestKeywords;
    }

}
