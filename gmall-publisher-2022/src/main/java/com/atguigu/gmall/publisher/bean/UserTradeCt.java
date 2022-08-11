package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
@Data
@AllArgsConstructor
public class UserTradeCt {
    // 交易类型
    String type;
    // 用户数
    Integer userCt;
}
