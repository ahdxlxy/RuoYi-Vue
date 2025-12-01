package com.exam.service;

import com.exam.dto.AnswerSubmitDTO;
import com.exam.vo.AnswerVO;

import java.util.List;

/**
 * 答题Service接口
 */
public interface AnswerService {
    
    void saveAnswer(AnswerSubmitDTO dto, Long studentId);
    
    void submitExam(Long examId, Long studentId);
    
    List<AnswerVO> getAnswersByExam(Long examId, Long studentId);
}
