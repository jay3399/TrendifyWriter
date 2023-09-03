package com.example.trendifywriter.domain.dailykeyword.service;

import com.example.trendifywriter.domain.dailykeyword.model.DailyKeyword;
import com.example.trendifywriter.domain.dailykeyword.repository.DailyKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyKeywordService {

    private final DailyKeywordRepository dailyKeywordRepository;


    public void save(DailyKeyword dailyKeyword) {

        dailyKeywordRepository.save(dailyKeyword);

    }


}
