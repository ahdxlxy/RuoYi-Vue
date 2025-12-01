package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 成绩实体
 */
@Data
@TableName("grade")
public class Grade implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 考试ID
     */
    private Long examId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 总分
     */
    private Integer totalScore;
    
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    
    /**
     * 状态：GRADING-阅卷中，COMPLETED-已完成
     */
    private String status;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
