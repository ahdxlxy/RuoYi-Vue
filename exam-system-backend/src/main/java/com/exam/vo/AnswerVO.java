package com.exam.vo;

import lombok.Data;

/**
 * 答题VO
 */
@Data
public class AnswerVO {
    
    private Long id;
    
    private Long examId;
    
    private Long studentId;
    
    private Long questionId;
    
    private String answer;
    
    private Integer score;
    
    /**
     * 题目信息
     */
    private QuestionVO question;
}
