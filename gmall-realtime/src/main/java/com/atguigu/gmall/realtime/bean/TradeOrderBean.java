package com.atguigu.gmall.realtime.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TradeOrderBean {
    // 窗口起始时间
    String stt;

    // 窗口关闭时间
    String edt;

    // 当天日期
    String curDate;

    // 下单独立用户数
    Long orderUniqueUserCount;

    // 下单新用户数
    Long orderNewUserCount;
}
