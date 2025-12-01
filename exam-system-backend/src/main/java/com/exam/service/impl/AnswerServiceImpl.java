package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exam.common.exception.BusinessException;
import com.exam.dto.AnswerSubmitDTO;
import com.exam.entity.Answer;
import com.exam.entity.Grade;
import com.exam.entity.PaperQuestion;
import com.exam.entity.Question;
import com.exam.mapper.*;
import com.exam.service.AnswerService;
import com.exam.vo.AnswerVO;
import com.exam.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerMapper answerMapper;
    private final QuestionMapper questionMapper;
    private final PaperQuestionMapper paperQuestionMapper;
    private final ExamMapper examMapper;
    private final GradeMapper gradeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAnswer(AnswerSubmitDTO dto, Long studentId) {
        // 检查是否已存在
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getExamId, dto.getExamId());
        wrapper.eq(Answer::getStudentId, studentId);
        wrapper.eq(Answer::getQuestionId, dto.getQuestionId());

        Answer answer = answerMapper.selectOne(wrapper);
        if (answer == null) {
            answer = new Answer();
            answer.setExamId(dto.getExamId());
            answer.setStudentId(studentId);
            answer.setQuestionId(dto.getQuestionId());
            answer.setAnswer(dto.getAnswer());
            answerMapper.insert(answer);
        } else {
            answer.setAnswer(dto.getAnswer());
            answerMapper.updateById(answer);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitExam(Long examId, Long studentId) {
        // 获取考试信息以得到试卷ID
        com.exam.entity.Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }

        // 自动判卷
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getExamId, examId);
        wrapper.eq(Answer::getStudentId, studentId);
        List<Answer> answers = answerMapper.selectList(wrapper);

        int totalScore = 0;
        boolean hasEssay = false;

        for (Answer answer : answers) {
            Question question = questionMapper.selectById(answer.getQuestionId());
            if (question == null)
                continue;

            // 获取题目分值
            LambdaQueryWrapper<PaperQuestion> pqWrapper = new LambdaQueryWrapper<>();
            pqWrapper.eq(PaperQuestion::getQuestionId, question.getId());
            pqWrapper.eq(PaperQuestion::getPaperId, exam.getPaperId());
            PaperQuestion pq = paperQuestionMapper.selectOne(pqWrapper);
            int questionScore = pq != null ? pq.getScore() : 0;

            // 自动判分
            if ("SINGLE".equals(question.getType()) || "JUDGE".equals(question.getType())) {
                if (question.getAnswer().equals(answer.getAnswer())) {
                    answer.setScore(questionScore);
                    totalScore += questionScore;
                } else {
                    answer.setScore(0);
                }
                answerMapper.updateById(answer);
            } else if ("BLANK".equals(question.getType())) {
                if (question.getAnswer().equalsIgnoreCase(answer.getAnswer().trim())) {
                    answer.setScore(questionScore);
                    totalScore += questionScore;
                } else {
                    answer.setScore(0);
                }
                answerMapper.updateById(answer);
            } else if ("ESSAY".equals(question.getType())) {
                hasEssay = true;
                answer.setScore(null); // 待人工阅卷
                answerMapper.updateById(answer);
            }
        }

        // 更新成绩
        LambdaQueryWrapper<Grade> gradeWrapper = new LambdaQueryWrapper<>();
        gradeWrapper.eq(Grade::getExamId, examId);
        gradeWrapper.eq(Grade::getStudentId, studentId);
        Grade grade = gradeMapper.selectOne(gradeWrapper);

        if (grade != null) {
            grade.setTotalScore(totalScore);
            grade.setSubmitTime(LocalDateTime.now());
            grade.setStatus(hasEssay ? "GRADING" : "COMPLETED");
            gradeMapper.updateById(grade);
        }
    }

    @Override
    public List<AnswerVO> getAnswersByExam(Long examId, Long studentId) {
        LambdaQueryWrapper<Answer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Answer::getExamId, examId);
        wrapper.eq(Answer::getStudentId, studentId);
        List<Answer> answers = answerMapper.selectList(wrapper);

        return answers.stream().map(answer -> {
            AnswerVO vo = BeanUtil.copyProperties(answer, AnswerVO.class);
            Question question = questionMapper.selectById(answer.getQuestionId());
            if (question != null) {
                vo.setQuestion(BeanUtil.copyProperties(question, QuestionVO.class));
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
