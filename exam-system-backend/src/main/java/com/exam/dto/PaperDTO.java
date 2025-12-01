package com.exam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 试卷DTO
 */
@Data
public class PaperDTO {
    
    private Long id;
    
    @NotBlank(message = "试卷名称不能为空")
    private String name;
    
    private String subject;
    
    /**
     * 试卷题目列表
     */
    private List<PaperQuestionItem> questions;
    
    @Data
    public static class PaperQuestionItem {
        private Long questionId;
        private Integer score;
        private Integer sort;
    }
}
