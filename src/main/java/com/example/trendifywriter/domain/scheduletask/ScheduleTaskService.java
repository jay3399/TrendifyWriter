package com.example.trendifywriter.domain.scheduletask;

import com.example.trendifywriter.application.service.FrequencyTrendAnalyzer;
import com.example.trendifywriter.application.web.WebSocketController;
import java.util.Map;
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

    private final RedisTemplate<String, Object> redisTemplate;

    private final ApplicationEventPublisher eventPublisher;


    // 이벤트 - 발행 구독 모델사용  컨트롤러 / 서비스 분리

    @Scheduled(fixedRate = 20000)  //20 초마다 키워드 추출로직 실행후 , 레디스에 저장
    public void updateRedis() {
        Map<String, Integer> latestKeywords = frequencyTrendAnalyzer.analyze();
        System.out.println("latestKeywords = " + latestKeywords);
        redisTemplate.opsForHash().putAll("realtime_keywords", latestKeywords);

        Map<Object, Object> keywords = redisTemplate.opsForHash().entries("realtime_keywords");

        eventPublisher.publishEvent(new KeywordsUpdatedEvent(keywords));

    }


}
