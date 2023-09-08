package com.example.trendifywriter.application.web;

import com.example.trendifywriter.application.dto.HourlyRefreshDTO;
import com.example.trendifywriter.application.dto.CurrentKeywordDTO;
import com.example.trendifywriter.domain.currentkeyword.service.CurrentSchService;
import com.example.trendifywriter.domain.dailykeyword.service.DailySchService;
import com.example.trendifywriter.domain.hourlykeyword.service.HourlySchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class WebSocketController {

    private final CurrentSchService currentSchService;

    private final HourlySchService hourlySchService;


    @GetMapping("/realtime_keywords")
    public Map<String , Object> realtimeKeywordDtoList() {


//        List<CurrentKeywordDTO> realList = scheduleTaskService.getRealList();

        List<CurrentKeywordDTO> realList = currentSchService.getRealList();

//        HourlyRefreshDTO refreshDTO = scheduleTaskService.getRefreshDTO();

        HourlyRefreshDTO refreshDTO = hourlySchService.getRefreshDTO();

        Map<String, Object> response = new HashMap<>();

        response.put("realList", realList);
        response.put("refreshDTO", refreshDTO);

        return response;

    }




}
