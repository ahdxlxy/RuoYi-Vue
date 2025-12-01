package com.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷VO
 */
@Data
public class PaperVO {
    
    private Long id;
    
    private String name;
    
    private String subject;
    
    private Integer totalScore;
    
    private LocalDateTime createTime;
    
    /**
     * 题目列表
     */
    private List<PaperQuestionVO> questions;
}
