package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/20
 */
@Data
@AllArgsConstructor
public class TrademarkOrderAmountPieGraph {
    // 品牌名称
    String trademarkName;
    // 销售额
    Double orderAmount;
}
