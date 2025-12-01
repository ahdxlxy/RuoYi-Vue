package com.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 班级VO
 */
@Data
public class ClassVO {
    
    private Long id;
    
    private String name;
    
    private String grade;
    
    private String profession;
    
    private LocalDateTime createTime;
    
    /**
     * 学生数量
     */
    private Integer studentCount;
}
