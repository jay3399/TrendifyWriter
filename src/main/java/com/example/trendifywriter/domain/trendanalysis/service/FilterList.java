package com.example.trendifywriter.domain.trendanalysis.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FilterList {

    private static final Set<String> filterList = new HashSet<>(Arrays.asList(
            "통해","모든", "지난", "일부", "혐의", "대한" ,"유튜브","측은","이날","기자","앵커","위반","수사","조사","직원","선수",
            "위해","조사","결과","현재","사실","이번","관련","당시","여부","앞서","다른","정부","경제","대표","전날",
            "가장","우리","최근","오늘","이전","올해","오후","이후","오전","저녁","이념","논란","있는","가운데","최대",
            "사건","사람","한겨레","대해","기술","세계","중이","지역","서울","전국","분야","그림판","요즘","바로","불법",
            "남자","여자","여성","남성","한국","내년","현장","저희","변경","자녀","직접","수년","검토","소식","사진","국내",
            "외국","지원","기업","최고","최악","경우","추기","가격","해당","지난달","이용","내용"));


    public static boolean shouldFilter(String word) {
        return filterList.contains(word);
    }


}
