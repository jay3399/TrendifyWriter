package com.example.trendifywriter.application.web;

import com.example.trendifywriter.application.dto.RealtimeKeywordDto;
import com.example.trendifywriter.application.service.ScheduleTaskService;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class WebSocketEventListener {


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ScheduleTaskService scheduleTaskService;

    public WebSocketEventListener(SimpMessagingTemplate simpMessagingTemplate, ScheduleTaskService scheduleTaskService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.scheduleTaskService = scheduleTaskService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        List<RealtimeKeywordDto> initialData = scheduleTaskService.getRealList();
        System.out.println("initialData = " + initialData);
        simpMessagingTemplate.convertAndSend("/topic/realtime_keywords", initialData);
    }




}
