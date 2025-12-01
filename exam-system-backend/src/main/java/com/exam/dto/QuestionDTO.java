package com.exam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 题目DTO
 */
@Data
public class QuestionDTO {
    
    private Long id;
    
    @NotBlank(message = "题型不能为空")
    private String type;
    
    private String subject;
    
    @NotBlank(message = "题目内容不能为空")
    private String content;
    
    /**
     * 选项（JSON字符串）
     */
    private String options;
    
    @NotBlank(message = "答案不能为空")
    private String answer;
    
    private String difficulty;
}
