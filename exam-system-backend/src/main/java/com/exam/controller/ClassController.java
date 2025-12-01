package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.result.Result;
import com.exam.dto.ClassDTO;
import com.exam.service.ClassService;
import com.exam.vo.ClassVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级Controller
 */
@RestController
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    /**
     * 分页查询班级列表
     */
    @GetMapping("/list")
    public Result<Page<ClassVO>> getClassList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<ClassVO> page = classService.getClassList(current, size, keyword);
        return Result.success(page);
    }

    /**
     * 获取所有班级（不分页）
     */
    @GetMapping("/all")
    public Result<List<ClassVO>> getAllClasses() {
        List<ClassVO> list = classService.getAllClasses();
        return Result.success(list);
    }

    /**
     * 创建班级
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> createClass(@Valid @RequestBody ClassDTO classDTO) {
        classService.createClass(classDTO);
        return Result.success();
    }

    /**
     * 更新班级
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> updateClass(@PathVariable Long id, @Valid @RequestBody ClassDTO classDTO) {
        classDTO.setId(id);
        classService.updateClass(classDTO);
        return Result.success();
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
        return Result.success();
    }

    /**
     * 添加学生到班级
     */
    @PostMapping("/{classId}/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> addStudentsToClass(@PathVariable Long classId, @RequestBody List<Long> studentIds) {
        classService.addStudentsToClass(classId, studentIds);
        return Result.success();
    }

    /**
     * 从班级移除学生
     */
    @DeleteMapping("/{classId}/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> removeStudentsFromClass(@PathVariable Long classId, @RequestBody List<Long> studentIds) {
        classService.removeStudentsFromClass(classId, studentIds);
        return Result.success();
    }

    /**
     * 获取当前学生所在的班级
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<List<ClassVO>> getMyClass() {
        Long studentId = getCurrentUserId();
        return Result.success(classService.getMyClass(studentId));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
