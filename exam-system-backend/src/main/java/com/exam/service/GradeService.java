package com.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.vo.GradeVO;

import java.util.List;

/**
 * 成绩Service接口
 */
public interface GradeService {
    
    Page<GradeVO> getGradeList(Integer current, Integer size, Long examId, Long studentId);
    
    List<GradeVO> getStudentGrades(Long studentId);
    
    GradeVO getGradeDetail(Long examId, Long studentId);
    
    void manualGrade(Long examId, Long studentId, Long questionId, Integer score);
}
