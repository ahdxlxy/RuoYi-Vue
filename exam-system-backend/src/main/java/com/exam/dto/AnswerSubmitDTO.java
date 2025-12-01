package com.exam.dto;

import lombok.Data;

/**
 * 答案提交DTO
 */
@Data
public class AnswerSubmitDTO {
    
    private Long examId;
    
    private Long questionId;
    
    private String answer;
}
