package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.exception.BusinessException;
import com.exam.dto.LoginDTO;
import com.exam.dto.UserDTO;
import com.exam.entity.User;
import com.exam.mapper.UserMapper;
import com.exam.service.UserService;
import com.exam.utils.JwtUtil;
import com.exam.vo.LoginVO;
import com.exam.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole());
        
        // 构建响应
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
        
        return loginVO;
    }
    
    @Override
    public UserVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    @Override
    public Page<UserVO> getUserList(Integer current, Integer size, String role, String keyword) {
        Page<User> page = new Page<>(current, size);
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(role), User::getRole, role);
        wrapper.and(StringUtils.hasText(keyword), w -> w
                .like(User::getUsername, keyword)
                .or()
                .like(User::getRealName, keyword));
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        
        // 转换为VO
        Page<UserVO> voPage = new Page<>();
        BeanUtil.copyProperties(userPage, voPage, "records");
        List<UserVO> voList = userPage.getRecords().stream()
                .map(user -> BeanUtil.copyProperties(user, UserVO.class))
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    public void createUser(UserDTO userDTO) {
        // 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = BeanUtil.copyProperties(userDTO, User.class);
        // 加密密码（默认密码为123456）
        String defaultPassword = StringUtils.hasText(userDTO.getPassword()) ? 
                userDTO.getPassword() : "123456";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        
        userMapper.insert(user);
    }
    
    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新用户信息
        BeanUtil.copyProperties(userDTO, user, "password");
        
        // 如果提供了新密码，则更新密码
        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        userMapper.updateById(user);
    }
    
    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
}
