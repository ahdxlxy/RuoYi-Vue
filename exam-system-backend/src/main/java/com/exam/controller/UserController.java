package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.result.Result;
import com.exam.dto.LoginDTO;
import com.exam.dto.UserDTO;
import com.exam.service.UserService;
import com.exam.vo.LoginVO;
import com.exam.vo.UserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/auth/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/info")
    public Result<UserVO> getUserInfo() {
        Long userId = getCurrentUserId();
        UserVO userVO = userService.getUserInfo(userId);
        return Result.success(userVO);
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping("/user/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Page<UserVO>> getUserList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String keyword) {
        Page<UserVO> page = userService.getUserList(current, size, role, keyword);
        return Result.success(page);
    }
    
    /**
     * 创建用户
     */
    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return Result.success();
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        userService.updateUser(userDTO);
        return Result.success();
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
    
    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
