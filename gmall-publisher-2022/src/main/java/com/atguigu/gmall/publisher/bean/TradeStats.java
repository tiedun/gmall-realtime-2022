package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
@Data
@AllArgsConstructor
public class TradeStats {
    // 指标类型
    String type;
    // 度量值
    Integer orderCt;
}
