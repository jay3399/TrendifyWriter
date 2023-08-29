package com.example.trendifywriter.domain.trendanalysis.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FilterList {

    private static final Set<String> filterList = new HashSet<>(Arrays.asList(
            "통해", "지난", "일부", "혐의", "대한" ,"유튜브","측은","이날","기자","앵커","위반","수사","조사",
            "위해","조사","결과","현재","사실","이번","관련","당시","여부","앞서","다른","정부","경제","대표",
            "가장","우리","최근","오늘","이전","오후","이후","오전","저녁","이념","논란","있는","가운데","최대",
            "사건","사람","한겨레","대해","기술","세계","중이","지역","서울","전국","분야"));


    public static boolean shouldFilter(String word) {
        return filterList.contains(word);
    }


}
