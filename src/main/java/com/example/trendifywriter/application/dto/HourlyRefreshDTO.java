package com.example.trendifywriter.application.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HourlyRefreshDTO {

    private String dataTime;

    private Map<String, Integer> data = new HashMap<>();

    private Map<String, Map<String, Integer>> allPreviousData = new HashMap<>();





}
