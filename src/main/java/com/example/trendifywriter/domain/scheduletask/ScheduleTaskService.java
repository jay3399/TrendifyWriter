package com.example.trendifywriter.domain.scheduletask;

import com.example.trendifywriter.application.service.FrequencyTrendAnalyzer;
import com.example.trendifywriter.application.web.WebSocketController;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleTaskService {

    private final FrequencyTrendAnalyzer frequencyTrendAnalyzer;
    private final ApplicationEventPublisher eventPublisher;

    private final RedisTemplate redisTemplate;

    // 이벤트 - 발행 구독 모델사용  컨트롤러 / 서비스 분리

    @Scheduled(fixedRate = 600000)  // 10분마다 실행
    public void updateTenMinutes() {
        Map<String, Integer> latestKeywords = frequencyTrendAnalyzer.analyze();
        eventPublisher.publishEvent(new KeywordsUpdatedEvent(latestKeywords));

        // 시간별 키워드 저장 로직
        String currentHour = getCurrentHour();
        redisTemplate.opsForHash().putAll("hourly:" + currentHour, latestKeywords);
    }

    @Scheduled(cron = "0 0 * * * *")  // 매시간 0분에 실행
    public void updateHourly() {
        String currentHour = getCurrentHour();
        Map<Object, Object> hourlyData = redisTemplate.opsForHash()
                .entries("hourly:" + currentHour);

        Map<String, Integer> hourlyTopKeywords = calculateTopKeywords(hourlyData);

        redisTemplate.opsForHash().putAll("daily:" + LocalDate.now(), hourlyTopKeywords);
    }

//    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
//    public void updateDaily() {
//        Map<Object, Object> dailyData = redisTemplate.opsForHash().entries("daily:" + LocalDate.now());
//
//
//    }

    private String getCurrentHour() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
    }


    private Map<String, Integer> calculateTopKeywords(Map<Object, Object>  range) {
        Map<String, Integer> aggregatedMap = new HashMap<>();

        return aggregatedMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)  // 상위 10개만 선택
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


    }
}


