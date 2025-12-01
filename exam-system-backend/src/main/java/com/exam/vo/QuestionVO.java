package com.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题目VO
 */
@Data
public class QuestionVO {
    
    private Long id;
    
    private String type;
    
    private String subject;
    
    private String content;
    
    private String options;
    
    private String answer;
    
    private String difficulty;
    
    private Long teacherId;
    
    private LocalDateTime createTime;
}
