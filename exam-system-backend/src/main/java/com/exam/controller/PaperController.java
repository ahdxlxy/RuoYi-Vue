package com.exam.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.result.Result;
import com.exam.dto.PaperDTO;
import com.exam.service.PaperService;
import com.exam.vo.PaperVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paper")
@RequiredArgsConstructor
public class PaperController {
    
    private final PaperService paperService;
    
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Page<PaperVO>> getPaperList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(paperService.getPaperList(current, size, keyword));
    }
    
    @GetMapping("/{id}")
    public Result<PaperVO> getPaperById(@PathVariable Long id) {
        return Result.success(paperService.getPaperById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> createPaper(@Valid @RequestBody PaperDTO paperDTO) {
        paperService.createPaper(paperDTO);
        return Result.success();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> updatePaper(@PathVariable Long id, @Valid @RequestBody PaperDTO paperDTO) {
        paperDTO.setId(id);
        paperService.updatePaper(paperDTO);
        return Result.success();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> deletePaper(@PathVariable Long id) {
        paperService.deletePaper(id);
        return Result.success();
    }
}
