package com.example.trendifywriter.application.web;

import com.example.trendifywriter.application.service.KeywordsEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @EventListener
    public void sendKeywords(KeywordsEvent event) {

        Map<String, Integer> keywords = event.getLatestKeywords();

        List<RealtimeKeywordDto> collect = getRealtimeKeywordDtos(keywords);

        simpMessagingTemplate.convertAndSend("/topic/realtime_keywords", collect
        );
    }

    private static List<RealtimeKeywordDto> getRealtimeKeywordDtos(Map<String, Integer> keywords) {
        List<RealtimeKeywordDto> collect = keywords.entrySet().stream()
                .map(entry -> new RealtimeKeywordDto( entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(RealtimeKeywordDto::getFrequency).reversed())
                .limit(10)
                .collect(Collectors.toList());
        return collect;
    }

//    @MessageMapping("/request_keywords")
//    @SendTo("/topic/realtime_keywords")
//    public List<RealtimeKeywordDto> sendKeywords() {
//
//        Map<Object, Object> keywords = redisTemplate.opsForHash().entries("realtime_keywords");
//
//        System.out.println("keywords = " + keywords);
//
//        return keywords.entrySet().stream()
//                .map(entry -> new RealtimeKeywordDto((String) entry.getKey(), (Integer) entry.getValue()))
//                .sorted(Comparator.comparingInt(RealtimeKeywordDto::getFrequency))
//                .collect(Collectors.toList());
//
//
//    }


}
