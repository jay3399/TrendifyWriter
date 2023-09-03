package com.example.trendifywriter.application.service;

import com.example.trendifywriter.domain.dailykeyword.model.DailyKeyword;
import com.example.trendifywriter.domain.dailykeyword.service.DailyKeywordService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleTaskService {

    private final FrequencyTrendAnalyzer frequencyTrendAnalyzer;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate redisTemplate;
    private final DailyKeywordService service;

    // 이벤트 - 발행 구독 모델사용  컨트롤러 / 서비스 분리

    @Scheduled(fixedRate = 300000)  // 5분마다 실행
    public void updateTenMinutes() {
        Map<String, Integer> latestKeywords = frequencyTrendAnalyzer.analyze();
        eventPublisher.publishEvent(new KeywordsEvent(latestKeywords)); // 실시간(10분간격) 키워드 웹소켓 업데이트.

        // 시간별 키워드 저장 로직
        // 10분별 데이터도 시각화가 필요할지 ????
        String currentHour = getCurrentHour();
        redisTemplate.opsForHash().putAll("hourly:" + currentHour, latestKeywords);
    }


    //"hourly:" + currentHour 부분에서 , 예를들어 13시부터 ~14시까지 10분씩 데이터를 넣고 , hourly:13
    // 시간 바뀌기 1초전에 getCurrentHour 을 해야 이전 HH 인 hourly:13 을불러서 넣을수 있다.  0분에 실행하면 안될듯.
    // calculateTopKeywords 메서드 구현필요 -> 탑 10 키워드 정렬 추출.
    // 시간별 데이터 시각화 어떤식으로 프론트에 구현할지 ? , redis말고 rdb는 필요없을지 ?
    @Scheduled(cron = "0 0 * * * *")  // 매시간 0분에 실행
    public void updateHourly() {

        String currentHour = getCurrentHour();
        Map<Object, Object> hourlyData = redisTemplate.opsForHash().entries("hourly:" + currentHour);

        redisTemplate.delete("hourly:" + currentHour);

        Map<String, Integer> hourlyTopKeywords = calculateTopKeywords(hourlyData);

        eventPublisher.publishEvent(new HourlyKeywordsEvent(hourlyTopKeywords));

        redisTemplate.opsForHash().putAll("daily:" + currentHour, hourlyTopKeywords);
    }

    // 이것도 1초전에 데이터 집계해야할듯
    //  모든 daily 값 가져와서 , calculateTopKeywords 로 추출 .
    // 일별 데이터와 시간별데이터는 , rdb에 저장후 , 데이터시각화에 보여줘야할듯 ? ,
    // 10분데이터는 실시간 키워드 보여줄때만 같이 보여주고  ,rdb에 저장은 안하고 시간별데이터 집게할떄 redis에서 삭제 , 즉 1시간동안 10분데이터를 6번 히스토리 까지만 보여주고 , 삭제 !
    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 실행
    public void updateDaily() {

        Map<Object, Object> daily = redisTemplate.opsForHash().entries("daily");

        Map<String, Integer> stringIntegerMap = calculateTopKeywords(daily);

        stringIntegerMap.entrySet().stream().forEachOrdered(
                entry -> {
                    DailyKeyword dailyKeyword = DailyKeyword.create(entry.getKey(),
                            entry.getValue());
                    service.save(dailyKeyword);
                }
        );
        redisTemplate.delete("daily");
    }


    private String getCurrentHour() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
    }


    private Map<String, Integer> calculateTopKeywords(Map<Object, Object>  data) {

        Map<String, Integer> stringIntegerMap = new HashMap<>();

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            stringIntegerMap.put((String) entry.getKey(), (Integer) entry.getValue());
        }

        LinkedHashMap<String, Integer> topKeywords = new LinkedHashMap<>();


        stringIntegerMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .forEachOrdered(entry -> topKeywords.put(entry.getKey(), entry.getValue()));

        return topKeywords;

    }
}


