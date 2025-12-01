package com.exam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 班级DTO
 */
@Data
public class ClassDTO {
    
    private Long id;
    
    @NotBlank(message = "班级名称不能为空")
    private String name;
    
    private String grade;
    
    private String profession;
}
