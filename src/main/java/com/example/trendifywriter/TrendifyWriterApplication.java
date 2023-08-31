package com.example.trendifywriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrendifyWriterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrendifyWriterApplication.class, args);
    }

}
