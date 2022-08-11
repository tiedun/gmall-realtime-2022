package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
@Data
@AllArgsConstructor
public class UserPageCt {
    // 页面 id
    String pageId;
    //独立访客数
    Integer uvCt;
}
