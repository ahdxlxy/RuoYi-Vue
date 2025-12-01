package com.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.dto.LoginDTO;
import com.exam.dto.UserDTO;
import com.exam.vo.LoginVO;
import com.exam.vo.UserVO;

/**
 * 用户Service接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * 获取用户信息
     */
    UserVO getUserInfo(Long userId);
    
    /**
     * 分页查询用户列表
     */
    Page<UserVO> getUserList(Integer current, Integer size, String role, String keyword);
    
    /**
     * 创建用户
     */
    void createUser(UserDTO userDTO);
    
    /**
     * 更新用户
     */
    void updateUser(UserDTO userDTO);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
}
