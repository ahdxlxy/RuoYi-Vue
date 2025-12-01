package com.exam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户DTO
 */
@Data
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    private String password;
    
    private String realName;
    
    @NotBlank(message = "角色不能为空")
    private String role;
    
    private String email;
    
    private String phone;
    
    private String gender;
}
