package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 题目实体
 */
@Data
@TableName("question")
public class Question implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 题目ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 题型：SINGLE-单选，MULTIPLE-多选，JUDGE-判断，BLANK-填空，ESSAY-简答
     */
    private String type;
    
    /**
     * 科目
     */
    private String subject;
    
    /**
     * 题目内容
     */
    private String content;
    
    /**
     * 选项（JSON格式）
     */
    private String options;
    
    /**
     * 答案
     */
    private String answer;
    
    /**
     * 难度：EASY-简单，MEDIUM-中等，HARD-困难
     */
    private String difficulty;
    
    /**
     * 创建教师ID
     */
    private Long teacherId;
    
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
    
    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer isDeleted;
}
