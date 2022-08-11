package com.atguigu.gmall.publisher.service;

import com.atguigu.gmall.publisher.bean.*;

import java.util.List;

/**
 * description:
 * Created by 铁盾 on 2022/4/17
 */
public interface TrafficChannelStatsService {
    List<TrafficUvCt> getUvCt(Integer date);

    List<TrafficSvCt> getSvCt(Integer date);

    List<TrafficPvPerSession> getPvPerSession(Integer date);

    List<TrafficDurPerSession> getDurPerSession(Integer date);

    List<TrafficUjRate> getUjRate(Integer date);
}
