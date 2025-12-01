package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.result.Result;
import com.exam.service.GradeService;
import com.exam.vo.GradeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Page<GradeVO>> getGradeList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) Long studentId) {
        return Result.success(gradeService.getGradeList(current, size, examId, studentId));
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<List<GradeVO>> getStudentGrades() {
        Long studentId = getCurrentUserId();
        return Result.success(gradeService.getStudentGrades(studentId));
    }

    @GetMapping("/detail")
    public Result<GradeVO> getGradeDetail(@RequestParam Long examId, @RequestParam(required = false) Long studentId) {
        if (studentId == null) {
            studentId = getCurrentUserId();
        }
        return Result.success(gradeService.getGradeDetail(examId, studentId));
    }

    @PostMapping("/mark")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<String> manualGrade(
            @RequestParam Long examId,
            @RequestParam Long studentId,
            @RequestParam Long questionId,
            @RequestParam Integer score) {
        gradeService.manualGrade(examId, studentId, questionId, score);
        return Result.success("评分成功");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
