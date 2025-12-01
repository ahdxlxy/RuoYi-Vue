package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.dto.AnswerSubmitDTO;
import com.exam.entity.*;
import com.exam.mapper.*;
import com.exam.service.AnswerService;
import com.exam.service.GradeService;
import com.exam.vo.GradeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    
    private final GradeMapper gradeMapper;
    private final ExamMapper examMapper;
    private final UserMapper userMapper;
    private final PaperMapper paperMapper;
    private final AnswerMapper answerMapper;
    
    @Override
    public Page<GradeVO> getGradeList(Integer current, Integer size, Long examId, Long studentId) {
        Page<Grade> page = new Page<>(current, size);
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(examId != null, Grade::getExamId, examId);
        wrapper.eq(studentId != null, Grade::getStudentId, studentId);
        wrapper.orderByDesc(Grade::getCreateTime);
        
        Page<Grade> gradePage = gradeMapper.selectPage(page, wrapper);
        Page<GradeVO> voPage = new Page<>();
        BeanUtil.copyProperties(gradePage, voPage, "records");
        List<GradeVO> voList = gradePage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
    
    @Override
    public List<GradeVO> getStudentGrades(Long studentId) {
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getStudentId, studentId);
        wrapper.orderByDesc(Grade::getCreateTime);
        List<Grade> grades = gradeMapper.selectList(wrapper);
        return grades.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public GradeVO getGradeDetail(Long examId, Long studentId) {
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getExamId, examId);
        wrapper.eq(Grade::getStudentId, studentId);
        Grade grade = gradeMapper.selectOne(wrapper);
        if (grade == null) {
            return null;
        }
        return convertToVO(grade);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualGrade(Long examId, Long studentId, Long questionId, Integer score) {
        // 更新答题得分
        LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.eq(Answer::getExamId, examId);
        answerWrapper.eq(Answer::getStudentId, studentId);
        answerWrapper.eq(Answer::getQuestionId, questionId);
        Answer answer = answerMapper.selectOne(answerWrapper);
        
        if (answer != null) {
            answer.setScore(score);
            answerMapper.updateById(answer);
        }
        
        // 重新计算总分
        LambdaQueryWrapper<Answer> allAnswersWrapper = new LambdaQueryWrapper<>();
        allAnswersWrapper.eq(Answer::getExamId, examId);
        allAnswersWrapper.eq(Answer::getStudentId, studentId);
        List<Answer> answers = answerMapper.selectList(allAnswersWrapper);
        
        int totalScore = answers.stream()
                .filter(a -> a.getScore() != null)
                .mapToInt(Answer::getScore)
                .sum();
        
        boolean allGraded = answers.stream().allMatch(a -> a.getScore() != null);
        
        // 更新成绩
        LambdaQueryWrapper<Grade> gradeWrapper = new LambdaQueryWrapper<>();
        gradeWrapper.eq(Grade::getExamId, examId);
        gradeWrapper.eq(Grade::getStudentId, studentId);
        Grade grade = gradeMapper.selectOne(gradeWrapper);
        
        if (grade != null) {
            grade.setTotalScore(totalScore);
            if (allGraded) {
                grade.setStatus("COMPLETED");
            }
            gradeMapper.updateById(grade);
        }
    }
    
    private GradeVO convertToVO(Grade grade) {
        GradeVO vo = BeanUtil.copyProperties(grade, GradeVO.class);
        
        Exam exam = examMapper.selectById(grade.getExamId());
        if (exam != null) {
            vo.setExamName(exam.getExamName());
            Paper paper = paperMapper.selectById(exam.getPaperId());
            if (paper != null) {
                vo.setPaperTotalScore(paper.getTotalScore());
            }
        }
        
        User student = userMapper.selectById(grade.getStudentId());
        if (student != null) {
            vo.setStudentName(student.getRealName());
        }
        
        return vo;
    }
}
