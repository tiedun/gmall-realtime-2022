package com.atguigu.gmall.publisher.service;

import com.atguigu.gmall.publisher.bean.CouponReduceStats;

import java.util.List;

/**
 * description:
 * Created by 铁盾 on 2022/4/19
 */
public interface CouponStatsService {
    List<CouponReduceStats> getCouponStats(Integer date);
}
