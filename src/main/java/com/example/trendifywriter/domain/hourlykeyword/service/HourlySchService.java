package com.example.trendifywriter.domain.hourlykeyword.service;

import com.example.trendifywriter.application.dto.HourlyRefreshDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HourlySchService {

    private final RedisTemplate redisTemplate;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private HourlyRefreshDTO refreshDTO;



    @Scheduled(fixedRate = 50000)
    public void updateHourly() {

        System.out.println("@@@@@");

        String currentHour = getCurrentHour();

        Map<Object, Object> hourlyData = getHourlyData(currentHour);


        Map<String, Integer> hourlyTopKeywords = calculateTopKeywords(hourlyData);


        Map<String, Map<String, Integer>> allPreviousData = getPreviousData(currentHour);

        updateRefreshDTO(new HourlyRefreshDTO(currentHour, hourlyTopKeywords, allPreviousData));

        sendHourlyData(currentHour, hourlyTopKeywords, allPreviousData);

//        redisTemplate.delete("hourly:" + currentHour);

        updateDailyDataInRedis(currentHour, hourlyTopKeywords);
    }

    private void updateDailyDataInRedis(String currentHour, Map<String, Integer> hourlyTopKeywords) {
        redisTemplate.opsForHash().putAll("daily:"+ currentHour, hourlyTopKeywords);
    }

    private void sendHourlyData(String currentHour, Map<String, Integer> hourlyTopKeywords,
            Map<String, Map<String, Integer>> allPreviousData) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("dataTime", currentHour);
        payload.put("data", hourlyTopKeywords);
        payload.put("allPreviousData", allPreviousData);

        simpMessagingTemplate.convertAndSend("/topic/hourly_data", payload);
    }

    private Map getHourlyData(String currentHour) {
        return redisTemplate.opsForHash().entries("hourly:" + currentHour);
    }

    public String getCurrentHour() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
    }

    private Map<String, Integer> calculateTopKeywords(Map<Object, Object>  data) {

        return data.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> (String) entry.getKey(),
                        entry -> (Integer) entry.getValue()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));

    }

    private void updateRefreshDTO(HourlyRefreshDTO newDTO) {
        this.refreshDTO = newDTO;
    }

    public HourlyRefreshDTO getRefreshDTO() {
        return refreshDTO;
    }

    private Map<String, Map<String, Integer>> getPreviousData(String currentHour) {

        Map<String, Map<String, Integer>> allPreviousData = new HashMap<>();
        for (int i = 0; i < Integer.parseInt(currentHour); i++) {
            String hour = String.format("%02d", i);
            Map<Object, Object> entries = redisTemplate.opsForHash().entries("daily:" + hour);
            allPreviousData.put(hour, calculateTopKeywords(entries));
        }
        return allPreviousData;
    }


}
