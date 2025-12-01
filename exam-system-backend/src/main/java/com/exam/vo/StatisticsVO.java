package com.exam.vo;

import lombok.Data;

/**
 * 首页统计数据VO
 */
@Data
public class StatisticsVO {
    /**
     * 用户总数
     */
    private Long userCount;

    /**
     * 试卷总数
     */
    private Long paperCount;

    /**
     * 考试总数
     */
    private Long examCount;

    /**
     * 题目总数
     */
    private Long questionCount;
}
