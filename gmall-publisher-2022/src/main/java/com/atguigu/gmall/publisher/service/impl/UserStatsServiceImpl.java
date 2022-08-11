package com.atguigu.gmall.publisher.service.impl;

import com.atguigu.gmall.publisher.bean.UserChangeCtPerType;
import com.atguigu.gmall.publisher.bean.UserPageCt;
import com.atguigu.gmall.publisher.bean.UserTradeCt;
import com.atguigu.gmall.publisher.mapper.UserStatsMapper;
import com.atguigu.gmall.publisher.service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
@Service
public class UserStatsServiceImpl implements UserStatsService {

    @Autowired
    UserStatsMapper userStatsMapper;

    @Override
    public List<UserPageCt> getUvByPage(Integer date) {
        return userStatsMapper.selectUvByPage(date);
    }

    @Override
    public List<UserChangeCtPerType> getUserChangeCt(Integer date) {
        return userStatsMapper.selectUserChangeCtPerType(date);
    }

    @Override
    public List<UserTradeCt> getTradeUserCt(Integer date) {
        return userStatsMapper.selectTradeUserCt(date);
    }


}
