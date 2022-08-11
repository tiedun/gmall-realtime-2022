package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/19
 */
@Data
@AllArgsConstructor
public class ActivityReduceStats {
    // 活动减免金额
    Double activityReduceAmount;
    // 原始金额
    Double originTotalAmount;
    // 活动补贴率
    Double activitySubsidyRate;
}
