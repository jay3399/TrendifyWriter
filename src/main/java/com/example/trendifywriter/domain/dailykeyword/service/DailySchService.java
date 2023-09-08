package com.example.trendifywriter.domain.dailykeyword.service;

import com.example.trendifywriter.domain.dailykeyword.model.DailyKeyword;
import com.example.trendifywriter.domain.dailykeyword.repository.DailyKeywordRepository;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailySchService {

    private final RedisTemplate redisTemplate;

    private final DailyKeywordRepository repository;

    private final SimpMessagingTemplate simpMessagingTemplate;



    public void updateDaily() {

        Map<Object, Object> daily = redisTemplate.opsForHash().entries("daily");

        Map<String, Integer> stringIntegerMap = calculateTopKeywords(daily);

        stringIntegerMap.entrySet().stream().forEachOrdered(
                entry -> {
                    DailyKeyword dailyKeyword = DailyKeyword.create(entry.getKey(), entry.getValue());
                    repository.save(dailyKeyword);
                }
        );

        redisTemplate.delete("daily");

        // 자정에 , 시간별 차트 초기화 .
        Map<String, Object> payload = new HashMap<>();
        payload.put("reset", true);
        simpMessagingTemplate.convertAndSend("/topic/hourly_data", payload);

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



}
