package com.exam.controller;

import com.exam.common.result.Result;
import com.exam.dto.AnswerSubmitDTO;
import com.exam.service.AnswerService;
import com.exam.vo.AnswerVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> saveAnswer(@RequestBody AnswerSubmitDTO dto) {
        Long studentId = getCurrentUserId();
        answerService.saveAnswer(dto, studentId);
        return Result.success();
    }

    @PostMapping("/submit/{examId}")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<Void> submitExam(@PathVariable Long examId) {
        Long studentId = getCurrentUserId();
        answerService.submitExam(examId, studentId);
        return Result.success("提交成功", null);
    }

    @GetMapping("/exam/{examId}")
    public Result<List<AnswerVO>> getAnswersByExam(@PathVariable Long examId,
            @RequestParam(required = false) Long studentId) {
        if (studentId == null) {
            studentId = getCurrentUserId();
        }
        return Result.success(answerService.getAnswersByExam(examId, studentId));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
