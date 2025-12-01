package com.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成绩VO
 */
@Data
public class GradeVO {
    
    private Long id;
    
    private Long examId;
    
    private Long studentId;
    
    private Integer totalScore;
    
    private LocalDateTime submitTime;
    
    private String status;
    
    /**
     * 考试名称
     */
    private String examName;
    
    /**
     * 学生姓名
     */
    private String studentName;
    
    /**
     * 试卷总分
     */
    private Integer paperTotalScore;
}
