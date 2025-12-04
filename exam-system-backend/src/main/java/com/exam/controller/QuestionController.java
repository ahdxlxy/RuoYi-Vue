package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.result.Result;
import com.exam.dto.QuestionDTO;
import com.exam.service.QuestionService;
import com.exam.vo.QuestionVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 题目Controller
 */
@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 分页查询题目列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Page<QuestionVO>> getQuestionList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String keyword) {
        Page<QuestionVO> page = questionService.getQuestionList(current, size, type, subject, keyword);
        return Result.success(page);
    }

    /**
     * 获取题目详情
     */
    @GetMapping("/{id}")
    public Result<QuestionVO> getQuestionById(@PathVariable Long id) {
        QuestionVO questionVO = questionService.getQuestionById(id);
        return Result.success(questionVO);
    }

    /**
     * 创建题目
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        Long teacherId = getCurrentUserId();
        questionService.createQuestion(questionDTO, teacherId);
        return Result.success();
    }

    /**
     * 更新题目
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionDTO questionDTO) {
        questionDTO.setId(id);
        questionService.updateQuestion(questionDTO);
        return Result.success();
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return Result.success();
    }

    /**
     * 导入题目
     */
    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<com.exam.dto.QuestionImportResultDTO> importQuestions(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            Long teacherId = getCurrentUserId();
            com.exam.dto.QuestionImportResultDTO result = questionService.importQuestions(file, teacherId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
