package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/19
 */
@Data
@AllArgsConstructor
public class CouponReduceStats {
    // 优惠券减免金额
    Double couponReduceAmount;
    // 原始金额
    Double originTotalAmount;
    // 优惠券补贴率
    Double couponSubsidyRate;
}
