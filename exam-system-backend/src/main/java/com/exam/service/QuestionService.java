package com.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.dto.QuestionDTO;
import com.exam.vo.QuestionVO;

/**
 * 题目Service接口
 */
public interface QuestionService {

    /**
     * 分页查询题目列表
     */
    Page<QuestionVO> getQuestionList(Integer current, Integer size, String type, String subject, String keyword);

    /**
     * 获取题目详情
     */
    QuestionVO getQuestionById(Long id);

    /**
     * 创建题目
     */
    void createQuestion(QuestionDTO questionDTO, Long teacherId);

    /**
     * 更新题目
     */
    void updateQuestion(QuestionDTO questionDTO);

    /**
     * 删除题目
     */
    void deleteQuestion(Long id);

    /**
     * 导入题目
     */
    com.exam.dto.QuestionImportResultDTO importQuestions(org.springframework.web.multipart.MultipartFile file,
            Long teacherId);
}
