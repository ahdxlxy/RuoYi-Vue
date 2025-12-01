package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.result.Result;
import com.exam.dto.ExamDTO;
import com.exam.service.ExamService;
import com.exam.vo.ExamVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
public class ExamController {
    
    private final ExamService examService;
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Page<ExamVO>> getExamList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(examService.getExamList(current, size, keyword));
    }
    
    @GetMapping("/available")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<List<ExamVO>> getAvailableExams() {
        Long studentId = getCurrentUserId();
        return Result.success(examService.getAvailableExams(studentId));
    }
    
    @GetMapping("/{id}")
    public Result<ExamVO> getExamById(@PathVariable Long id) {
        return Result.success(examService.getExamById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> createExam(@Valid @RequestBody ExamDTO examDTO) {
        examService.createExam(examDTO);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> updateExam(@PathVariable Long id, @Valid @RequestBody ExamDTO examDTO) {
        examDTO.setId(id);
        examService.updateExam(examDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return Result.success();
    }
    
    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<ExamVO> startExam(@PathVariable Long id, @RequestParam(required = false) String password) {
        Long studentId = getCurrentUserId();
        return Result.success(examService.startExam(id, studentId, password));
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
