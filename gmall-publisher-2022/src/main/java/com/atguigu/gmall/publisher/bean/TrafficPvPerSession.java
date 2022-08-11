package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
@Data
@AllArgsConstructor
public class TrafficPvPerSession {
    // 渠道
    String ch;
    // 各会话页面浏览数
    Double pvPerSession;
}
