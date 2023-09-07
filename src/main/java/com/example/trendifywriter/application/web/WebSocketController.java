package com.example.trendifywriter.application.web;

import com.example.trendifywriter.application.dto.RealtimeKeywordDto;
import com.example.trendifywriter.application.service.ScheduleTaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class WebSocketController {

    private final ScheduleTaskService scheduleTaskService;


    @GetMapping("/realtime_keywords")
    public List<RealtimeKeywordDto> realtimeKeywordDtoList() {


        List<RealtimeKeywordDto> realList = scheduleTaskService.getRealList();


        return realList;

    }




}
