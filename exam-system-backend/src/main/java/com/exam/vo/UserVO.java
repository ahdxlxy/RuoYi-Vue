package com.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户VO
 */
@Data
public class UserVO {
    
    private Long id;
    
    private String username;
    
    private String realName;
    
    private String role;
    
    private String email;
    
    private String phone;
    
    private String gender;
    
    private LocalDateTime createTime;
}
