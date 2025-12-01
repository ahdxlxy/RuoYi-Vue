package com.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试VO
 */
@Data
public class ExamVO {
    
    private Long id;
    
    private Long paperId;
    
    private String examName;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Integer duration;
    
    private Long classId;
    
    private Integer allowMakeup;
    
    private String password;
    
    private LocalDateTime createTime;
    
    /**
     * 试卷信息
     */
    private PaperVO paper;
    
    /**
     * 班级名称
     */
    private String className;
}
