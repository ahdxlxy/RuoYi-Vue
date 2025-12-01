package com.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.dto.ExamDTO;
import com.exam.vo.ExamVO;

import java.util.List;

/**
 * 考试Service接口
 */
public interface ExamService {
    
    Page<ExamVO> getExamList(Integer current, Integer size, String keyword);
    
    List<ExamVO> getAvailableExams(Long studentId);
    
    ExamVO getExamById(Long id);
    
    void createExam(ExamDTO examDTO);
    
    void updateExam(ExamDTO examDTO);
    
    void deleteExam(Long id);
    
    ExamVO startExam(Long examId, Long studentId, String password);
}
