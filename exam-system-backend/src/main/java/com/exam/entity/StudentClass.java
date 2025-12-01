package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学生班级关联实体
 */
@Data
@TableName("student_class")
public class StudentClass implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 班级ID
     */
    private Long classId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
