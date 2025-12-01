package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.exception.BusinessException;
import com.exam.dto.ExamDTO;
import com.exam.entity.*;
import com.exam.entity.Class;
import com.exam.mapper.*;
import com.exam.service.ExamService;
import com.exam.vo.ExamVO;
import com.exam.vo.PaperVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamMapper examMapper;
    private final PaperMapper paperMapper;
    private final ClassMapper classMapper;
    private final StudentClassMapper studentClassMapper;
    private final GradeMapper gradeMapper;

    @Override
    public Page<ExamVO> getExamList(Integer current, Integer size, String keyword) {
        Page<Exam> page = new Page<>(current, size);
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), Exam::getExamName, keyword);
        wrapper.orderByDesc(Exam::getCreateTime);

        Page<Exam> examPage = examMapper.selectPage(page, wrapper);
        Page<ExamVO> voPage = new Page<>();
        BeanUtil.copyProperties(examPage, voPage, "records");
        List<ExamVO> voList = examPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<ExamVO> getAvailableExams(Long studentId) {
        LocalDateTime now = LocalDateTime.now();

        // 查询学生所在班级
        LambdaQueryWrapper<StudentClass> scWrapper = new LambdaQueryWrapper<>();
        scWrapper.eq(StudentClass::getStudentId, studentId);
        List<StudentClass> studentClasses = studentClassMapper.selectList(scWrapper);
        List<Long> classIds = studentClasses.stream()
                .map(StudentClass::getClassId)
                .collect(Collectors.toList());

        // 查询可参加的考试
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        // wrapper.le(Exam::getStartTime, now); // 允许查看未开始的考试
        // wrapper.ge(Exam::getEndTime, now); // 允许查看已结束的考试
        wrapper.and(w -> w.isNull(Exam::getClassId)
                .or(classIds.isEmpty() ? w2 -> w2.eq(Exam::getClassId, -1) : w2 -> w2.in(Exam::getClassId, classIds)));

        List<Exam> exams = examMapper.selectList(wrapper);

        // 过滤已考试的
        return exams.stream().filter(exam -> {
            LambdaQueryWrapper<Grade> gradeWrapper = new LambdaQueryWrapper<>();
            gradeWrapper.eq(Grade::getExamId, exam.getId());
            gradeWrapper.eq(Grade::getStudentId, studentId);
            return gradeMapper.selectCount(gradeWrapper) == 0;
        }).map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public ExamVO getExamById(Long id) {
        Exam exam = examMapper.selectById(id);
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }
        return convertToVO(exam);
    }

    @Override
    public void createExam(ExamDTO examDTO) {
        Exam exam = BeanUtil.copyProperties(examDTO, Exam.class);
        examMapper.insert(exam);
    }

    @Override
    public void updateExam(ExamDTO examDTO) {
        Exam exam = examMapper.selectById(examDTO.getId());
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }
        BeanUtil.copyProperties(examDTO, exam);
        examMapper.updateById(exam);
    }

    @Override
    public void deleteExam(Long id) {
        examMapper.deleteById(id);
    }

    @Override
    public ExamVO startExam(Long examId, Long studentId, String password) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new BusinessException("考试不存在");
        }

        // 验证密码
        if (StringUtils.hasText(exam.getPassword()) && !exam.getPassword().equals(password)) {
            throw new BusinessException("考试密码错误");
        }

        // 验证时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime())) {
            throw new BusinessException("考试尚未开始");
        }
        if (now.isAfter(exam.getEndTime())) {
            throw new BusinessException("考试已结束");
        }

        // 检查是否已参加
        LambdaQueryWrapper<Grade> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grade::getExamId, examId);
        wrapper.eq(Grade::getStudentId, studentId);
        if (gradeMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("已参加过该考试");
        }

        // 创建成绩记录
        Grade grade = new Grade();
        grade.setExamId(examId);
        grade.setStudentId(studentId);
        grade.setTotalScore(0);
        grade.setStatus("GRADING");
        gradeMapper.insert(grade);

        return convertToVO(exam);
    }

    private ExamVO convertToVO(Exam exam) {
        ExamVO vo = BeanUtil.copyProperties(exam, ExamVO.class);
        Paper paper = paperMapper.selectById(exam.getPaperId());
        if (paper != null) {
            vo.setPaper(BeanUtil.copyProperties(paper, PaperVO.class));
        }
        if (exam.getClassId() != null) {
            Class clazz = classMapper.selectById(exam.getClassId());
            if (clazz != null) {
                vo.setClassName(clazz.getName());
            }
        }
        return vo;
    }
}
