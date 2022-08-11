package com.atguigu.gmall.publisher.service;

import com.atguigu.gmall.publisher.bean.TrafficKeywords;

import java.util.List;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
public interface TrafficKeywordsService {
    List<TrafficKeywords> getKeywords(Integer date);
}
