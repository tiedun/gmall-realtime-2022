package com.atguigu.gmall.publisher.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * description:
 * Created by 铁盾 on 2022/4/18
 */
@Data
@AllArgsConstructor
public class TrafficKeywords {
    // 关键词
    String keyword;
    // 关键词评分
    Integer keywordScore;
}
