package com.example.trendifywriter.domain.scheduletask;

import java.util.Map;

public class KeywordsUpdatedEvent {

    private final Map<Object, Object> latestKeywords;

    public KeywordsUpdatedEvent(Map<Object, Object> latestKeywords) {
        this.latestKeywords = latestKeywords;
    }

    public Map<Object, Object> getLatestKeywords() {
        return latestKeywords;
    }

}
