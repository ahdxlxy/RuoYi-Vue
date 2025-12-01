package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试卷实体
 */
@Data
@TableName("paper")
public class Paper implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 试卷ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 试卷名称
     */
    private String name;
    
    /**
     * 科目
     */
    private String subject;
    
    /**
     * 总分
     */
    private Integer totalScore;
    
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
