package com.example.trendifywriter.domain.currentkeyword.service;

import com.example.trendifywriter.application.dto.CurrentKeywordDTO;
import com.example.trendifywriter.application.service.TrendAnalysisService;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentSchService {

    private final TrendAnalysisService trendAnalysisService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisTemplate redisTemplate;
    private List<CurrentKeywordDTO> lastFetchedData = new ArrayList<>();
    private String currentHour;

    @PostConstruct  // 서비스 시작 시 한 번 실행
    public void init() {
        updateTenMinutes();
    }

    @Scheduled(fixedRate = 30000)  // 10분마다 실행 ,
    public void updateTenMinutes() {
        System.out.println("!!!!");

        setCurrentHour();

        Map<String, Integer> latestKeywords = getLatestKeywords();

        updateLastData(latestKeywords);

        sendCurrentKeywords();

        updateHourlyDataInRedis(latestKeywords);

    }

    private void updateHourlyDataInRedis(Map<String, Integer> latestKeywords) {
        String redisKey = "hourly:" + currentHour;

        HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();

        Map<String, Integer> existingValues = hashOperations.entries(redisKey);

        for (Entry<String, Integer> entry : latestKeywords.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            existingValues.merge(key, value, Integer::sum);
        }

        hashOperations.putAll(redisKey, existingValues);
    }

    private void sendCurrentKeywords() {
        simpMessagingTemplate.convertAndSend("/topic/realtime_keywords", lastFetchedData);
    }

    private Map<String, Integer> getLatestKeywords() {
        return trendAnalysisService.analyze();
    }

    private void setCurrentHour() {
        if (currentHour == null) {
            currentHour = getCurrentHour();
        }
    }

    public String getCurrentHour() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
    }

    private static List<CurrentKeywordDTO> getRealtimeKeywordDTO(Map<String, Integer> keywords) {
        List<CurrentKeywordDTO> collect = keywords.entrySet().stream()
                .map(entry -> new CurrentKeywordDTO( entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(CurrentKeywordDTO::getFrequency).reversed())
                .limit(10)
                .collect(Collectors.toList());
        return collect;
    }

    public  List<CurrentKeywordDTO> getRealList(
    ) {
        return Collections.unmodifiableList(lastFetchedData);
    }

    private void updateLastData(Map<String, Integer> latestKeywords) {

        List<CurrentKeywordDTO> realtimeKeywordDTO = getRealtimeKeywordDTO(latestKeywords);

        this.lastFetchedData.clear();
        this.lastFetchedData.addAll(realtimeKeywordDTO);
    }

    //    @Scheduled(cron = "59 59 * * * *")
    private void resetCurrentHour() {
        currentHour = null;
    }





}
