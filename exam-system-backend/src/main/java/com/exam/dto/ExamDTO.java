package com.exam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试DTO
 */
@Data
public class ExamDTO {
    
    private Long id;
    
    @NotNull(message = "试卷ID不能为空")
    private Long paperId;
    
    @NotBlank(message = "考试名称不能为空")
    private String examName;
    
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    
    @NotNull(message = "考试时长不能为空")
    private Integer duration;
    
    private Long classId;
    
    private Integer allowMakeup;
    
    private String password;
}
