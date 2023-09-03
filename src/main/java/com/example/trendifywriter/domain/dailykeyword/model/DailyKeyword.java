package com.example.trendifywriter.domain.dailykeyword.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailyKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;

    private Integer frequency;

    private LocalDate date;


    private void set(String key , Integer v) {
        this.keyword = key;
        this.frequency = v;
        this.date = LocalDate.now();
    }

    public static DailyKeyword create(String key, Integer value) {
        DailyKeyword dailyKeyword = new DailyKeyword();
        dailyKeyword.set(key, value);
        return dailyKeyword;
    }



}

