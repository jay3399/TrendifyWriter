package com.example.trendifywriter.application.service;

import com.example.trendifywriter.domain.dailykeyword.model.DailyKeyword;
import com.example.trendifywriter.domain.dailykeyword.service.DailyKeywordService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


// 흐름정리

// 10분주기 스케줄러 , 10분마다 키워드를 모아서 레디스 hourly:키에 결과를 누적시킨다 by updateTenMinutes()

// 1시간주기 스케줄러 , 1시간이 돨때 레디스에 누적시킨값을 꺼내서 프론트 차트용으로 쓰일 시간별 탑키워드 10개를계산한후 반환, 그리고 해당 hourly: 레디스키는삭제하고 새로만들어진값을 daily:키에저장  by updateHourly

// 자정이 넘어가기전에 , 그날 모든 redis 에 저장된 시간별 키워드 daily 값을 모두 꺼내서 일별 탑키워드 10개를구해서  해당 값을 rdb에 저장후 redis daily값 삭제 . 일별데이터 조회가능 .updateDaily

//



@Service
@RequiredArgsConstructor
public class ScheduleTaskService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FrequencyTrendAnalyzer frequencyTrendAnalyzer;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate redisTemplate;
    private final DailyKeywordService service;

    // 이벤트 - 발행 구독 모델사용  컨트롤러 / 서비스 분리

    @Scheduled(fixedRate = 30000)  // 5분마다 실행
    public void updateTenMinutes() {
        Map<String, Integer> latestKeywords = frequencyTrendAnalyzer.analyze();
        eventPublisher.publishEvent(new KeywordsEvent(latestKeywords)); // 실시간(10분간격) 키워드 웹소켓 업데이트.

        String currentHour = getCurrentHour();

        HashOperations<String, String, Integer> hashOperations = redisTemplate.opsForHash();

        for (Entry<String, Integer> entry : latestKeywords.entrySet()) {
            String key = entry.getKey();
            Integer newFrequency = entry.getValue();

            Integer existingFrequency = hashOperations.get("hourly:" +currentHour, key);

            if (existingFrequency != null) {
                newFrequency += existingFrequency;
            }

            hashOperations.put("hourly:" +currentHour, key, newFrequency);
        }

//        redisTemplate.opsForHash().putAll("hourly:" + currentHour, latestKeywords);
    }


    // 시간 바뀌기 1초전에 getCurrentHour 을 해야 이전 HH 인 hourly:13 을불러서 넣을수 있다.  0분에 실행하면 안될듯.
    // calculateTopKeywords 메서드 구현필요 -> 탑 10 키워드 정렬 추출.
    // 시간별 데이터 시각화 어떤식으로 프론트에 구현할지 ? , redis말고 rdb는 필요없을지 ?
//    @Scheduled(cron = "0 0 * * * *")  // 매시간 0분에 실행
    @Scheduled(fixedRate = 65000)
    public void updateHourly() {




        String currentHour = getCurrentHour();

//        String backupKey = "hourly_backup:" + currentHour;


        Map<Object, Object> hourlyData = redisTemplate.opsForHash().entries("hourly:" + currentHour);

//        redisTemplate.opsForHash().putAll(backupKey, hourlyData);

        System.out.println("hourlyData = " + hourlyData);


        Map<String, Integer> hourlyTopKeywords = calculateTopKeywords(hourlyData);

        System.out.println("hourlyTopKeywords = " + hourlyTopKeywords);

        Map<String, Map<String, Integer>> allPreviousData = new HashMap<>();

        for (int i = 0; i < Integer.parseInt(currentHour); i++) {
            String hour = String.format("%02d", i);
            Map<Object, Object> entries = redisTemplate.opsForHash().entries("daily:" + hour);
            System.out.println("entries = " + entries);
            allPreviousData.put(hour, calculateTopKeywords(entries));
        }


        Map<String, Object> payload = new HashMap<>();
        payload.put("dataTime", currentHour);
        payload.put("data", hourlyTopKeywords);
        payload.put("allPreviousData", allPreviousData);

        simpMessagingTemplate.convertAndSend("/topic/hourly_data", payload);

        redisTemplate.delete("hourly:" + currentHour);

        redisTemplate.opsForHash().putAll("daily:"+ currentHour, hourlyTopKeywords);
    }


    //자정에 레디스 시간별 값 꺼내서 , 일별키워드 산출후 rdb 에 저장 .
    @Scheduled(cron = "0 0 0 * * *")
    public void updateDaily() {

        Map<Object, Object> daily = redisTemplate.opsForHash().entries("daily");

        Map<String, Integer> stringIntegerMap = calculateTopKeywords(daily);

        stringIntegerMap.entrySet().stream().forEachOrdered(
                entry -> {
                    DailyKeyword dailyKeyword = DailyKeyword.create(entry.getKey(), entry.getValue());
                    service.save(dailyKeyword);
                }
        );

        redisTemplate.delete("daily");

        // 자정에 , 시간별 차트 초기화 .
        Map<String, Object> payload = new HashMap<>();
        payload.put("reset", true);
        simpMessagingTemplate.convertAndSend("/topic/hourly_data", payload);

    }


    private String getCurrentHour() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
    }

    public String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
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


