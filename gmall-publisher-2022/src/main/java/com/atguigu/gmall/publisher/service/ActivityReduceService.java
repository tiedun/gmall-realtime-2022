package com.atguigu.gmall.publisher.service;

import com.atguigu.gmall.publisher.bean.ActivityReduceStats;

import java.util.List;

/**
 * description:
 * Created by 铁盾 on 2022/4/19
 */
public interface ActivityReduceService {
    List<ActivityReduceStats> getActivityStats(Integer date);
}
