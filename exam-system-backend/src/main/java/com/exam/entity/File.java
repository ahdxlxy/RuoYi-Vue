package com.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件实体
 */
@Data
@TableName("file")
public class File implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 文件URL
     */
    private String url;
    
    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;
}
