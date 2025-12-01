package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 答题记录实体
 */
@Data
@TableName("answer")
public class Answer implements Serializable {
    
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
     * 题目ID
     */
    private Long questionId;
    
    /**
     * 学生答案
     */
    private String answer;
    
    /**
     * 得分
     */
    private Integer score;
    
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
