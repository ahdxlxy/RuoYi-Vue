package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考试实体
 */
@Data
@TableName("exam")
public class Exam implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 考试ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 试卷ID
     */
    private Long paperId;
    
    /**
     * 考试名称
     */
    private String examName;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 考试时长（分钟）
     */
    private Integer duration;
    
    /**
     * 班级ID（为空表示全体可参加）
     */
    private Long classId;
    
    /**
     * 允许补考：0-不允许，1-允许
     */
    private Integer allowMakeup;
    
    /**
     * 考试密码
     */
    private String password;
    
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
