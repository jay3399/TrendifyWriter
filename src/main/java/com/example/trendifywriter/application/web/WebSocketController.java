package com.example.trendifywriter.application.web;

import com.example.trendifywriter.domain.scheduletask.KeywordsUpdatedEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final RedisTemplate<String , Object> redisTemplate;

    private final SimpMessagingTemplate simpMessagingTemplate;


    @EventListener
    public void sendKeywords(KeywordsUpdatedEvent event) {

//        Map<Object, Object> keywords = redisTemplate.opsForHash().entries("realtime_keywords");
//
//        System.out.println("keywords = " + keywords);

        Map<Object, Object> keywords = event.getLatestKeywords();


        List<RealtimeKeywordDto> collect = keywords.entrySet().stream()
                .map(entry -> new RealtimeKeywordDto((String) entry.getKey(),
                        (Integer) entry.getValue()))
                .sorted(Comparator.comparingInt(RealtimeKeywordDto::getFrequency))
                .collect(Collectors.toList());

        simpMessagingTemplate.convertAndSend("/topic/realtime_keywords", collect
        );


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
