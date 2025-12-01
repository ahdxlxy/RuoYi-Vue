package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试卷题目关联实体
 */
@Data
@TableName("paper_question")
public class PaperQuestion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 试卷ID
     */
    private Long paperId;
    
    /**
     * 题目ID
     */
    private Long questionId;
    
    /**
     * 分数
     */
    private Integer score;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
