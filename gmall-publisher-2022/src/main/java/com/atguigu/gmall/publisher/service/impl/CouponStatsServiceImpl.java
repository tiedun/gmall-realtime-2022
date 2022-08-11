package com.atguigu.gmall.publisher.service.impl;

import com.atguigu.gmall.publisher.bean.CouponReduceStats;
import com.atguigu.gmall.publisher.mapper.CouponStatsMapper;
import com.atguigu.gmall.publisher.service.CouponStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description:
 * Created by 铁盾 on 2022/4/19
 */
@Service
public class CouponStatsServiceImpl implements CouponStatsService {

    @Autowired
    private CouponStatsMapper couponStatsMapper;

    @Override
    public List<CouponReduceStats> getCouponStats(Integer date) {
        return couponStatsMapper.selectCouponStats(date);
    }
}
