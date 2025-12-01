package com.exam.vo;

import lombok.Data;

/**
 * 试卷题目VO
 */
@Data
public class PaperQuestionVO {
    
    private Long id;
    
    private Long questionId;
    
    private Integer score;
    
    private Integer sort;
    
    /**
     * 题目详情
     */
    private QuestionVO question;
}
