package com.example.trendifywriter.application.web;

import com.example.trendifywriter.application.dto.RealtimeKeywordDto;
import com.example.trendifywriter.application.service.ScheduleTaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ScheduleTaskService scheduleTaskService;


    @SubscribeMapping("/topic/realtime_keywords")
    public List<RealtimeKeywordDto> realtimeKeywordDtoList() {

        System.out.println("호출!!");

        List<RealtimeKeywordDto> realList = scheduleTaskService.getRealList();

        System.out.println("realList = " + realList);

        return realList;

    }




}
