package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.exception.BusinessException;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionImportDTO;
import com.exam.entity.Question;
import com.exam.listener.QuestionImportListener;
import com.exam.mapper.QuestionMapper;
import com.exam.service.QuestionService;
import com.exam.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目Service实现类
 */
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;

    @Override
    public Page<QuestionVO> getQuestionList(Integer current, Integer size, String type, String subject,
            String keyword) {
        Page<Question> page = new Page<>(current, size);

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(type), Question::getType, type);
        wrapper.eq(StringUtils.hasText(subject), Question::getSubject, subject);
        wrapper.like(StringUtils.hasText(keyword), Question::getContent, keyword);
        wrapper.orderByDesc(Question::getCreateTime);

        Page<Question> questionPage = questionMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<QuestionVO> voPage = new Page<>();
        BeanUtil.copyProperties(questionPage, voPage, "records");
        List<QuestionVO> voList = questionPage.getRecords().stream()
                .map(question -> BeanUtil.copyProperties(question, QuestionVO.class))
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public QuestionVO getQuestionById(Long id) {
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new BusinessException("题目不存在");
        }
        return BeanUtil.copyProperties(question, QuestionVO.class);
    }

    @Override
    public void createQuestion(QuestionDTO questionDTO, Long teacherId) {
        Question question = BeanUtil.copyProperties(questionDTO, Question.class);
        question.setTeacherId(teacherId);
        questionMapper.insert(question);
    }

    @Override
    public void updateQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.selectById(questionDTO.getId());
        if (question == null) {
            throw new BusinessException("题目不存在");
        }
        BeanUtil.copyProperties(questionDTO, question);
        questionMapper.updateById(question);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionMapper.deleteById(id);
    }

    @Override
    public void importQuestions(MultipartFile file, Long teacherId) {
        try {
            EasyExcel.read(file.getInputStream(), QuestionImportDTO.class,
                    new QuestionImportListener(this, teacherId)).sheet().doRead();
        } catch (IOException e) {
            throw new BusinessException("文件读取失败");
        }
    }
}
