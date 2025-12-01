package com.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exam.common.result.Result;
import com.exam.mapper.ExamMapper;
import com.exam.mapper.PaperMapper;
import com.exam.mapper.QuestionMapper;
import com.exam.mapper.UserMapper;
import com.exam.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计Controller
 */
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final UserMapper userMapper;
    private final PaperMapper paperMapper;
    private final ExamMapper examMapper;
    private final QuestionMapper questionMapper;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/dashboard")
    public Result<StatisticsVO> getDashboardStats() {
        StatisticsVO vo = new StatisticsVO();

        // 获取用户总数
        vo.setUserCount(userMapper.selectCount(new LambdaQueryWrapper<>()));

        // 获取试卷总数
        vo.setPaperCount(paperMapper.selectCount(new LambdaQueryWrapper<>()));

        // 获取考试总数
        vo.setExamCount(examMapper.selectCount(new LambdaQueryWrapper<>()));

        // 获取题目总数
        vo.setQuestionCount(questionMapper.selectCount(new LambdaQueryWrapper<>()));

        return Result.success(vo);
    }
}
