package com.example.trendifywriter.application.service;

import java.util.Map;

public class HourlyKeywordsEvent {

    private final Map<String, Integer> hourlyTopKeywords;

    public HourlyKeywordsEvent(Map<String, Integer> hourlyTopKeywords) {
        this.hourlyTopKeywords = hourlyTopKeywords;
    }


}
