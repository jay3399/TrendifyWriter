package com.example.trendifywriter.domain.dailykeyword.repository;

import com.example.trendifywriter.domain.dailykeyword.model.DailyKeyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyKeywordRepository extends JpaRepository<DailyKeyword, Long> {



}
