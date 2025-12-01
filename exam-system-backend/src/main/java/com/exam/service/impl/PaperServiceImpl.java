package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.exception.BusinessException;
import com.exam.dto.PaperDTO;
import com.exam.entity.Paper;
import com.exam.entity.PaperQuestion;
import com.exam.entity.Question;
import com.exam.mapper.PaperMapper;
import com.exam.mapper.PaperQuestionMapper;
import com.exam.mapper.QuestionMapper;
import com.exam.service.PaperService;
import com.exam.vo.PaperQuestionVO;
import com.exam.vo.PaperVO;
import com.exam.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaperServiceImpl implements PaperService {
    
    private final PaperMapper paperMapper;
    private final PaperQuestionMapper paperQuestionMapper;
    private final QuestionMapper questionMapper;
    
    @Override
    public Page<PaperVO> getPaperList(Integer current, Integer size, String keyword) {
        Page<Paper> page = new Page<>(current, size);
        LambdaQueryWrapper<Paper> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), Paper::getName, keyword);
        wrapper.orderByDesc(Paper::getCreateTime);
        
        Page<Paper> paperPage = paperMapper.selectPage(page, wrapper);
        Page<PaperVO> voPage = new Page<>();
        BeanUtil.copyProperties(paperPage, voPage, "records");
        List<PaperVO> voList = paperPage.getRecords().stream()
                .map(paper -> BeanUtil.copyProperties(paper, PaperVO.class))
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
    
    @Override
    public PaperVO getPaperById(Long id) {
        Paper paper = paperMapper.selectById(id);
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }
        
        PaperVO vo = BeanUtil.copyProperties(paper, PaperVO.class);
        
        // 查询试卷题目
        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, id);
        wrapper.orderByAsc(PaperQuestion::getSort);
        List<PaperQuestion> paperQuestions = paperQuestionMapper.selectList(wrapper);
        
        List<PaperQuestionVO> questionVOs = paperQuestions.stream().map(pq -> {
            PaperQuestionVO pqVO = BeanUtil.copyProperties(pq, PaperQuestionVO.class);
            Question question = questionMapper.selectById(pq.getQuestionId());
            if (question != null) {
                pqVO.setQuestion(BeanUtil.copyProperties(question, QuestionVO.class));
            }
            return pqVO;
        }).collect(Collectors.toList());
        
        vo.setQuestions(questionVOs);
        return vo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPaper(PaperDTO paperDTO) {
        Paper paper = BeanUtil.copyProperties(paperDTO, Paper.class);
        paper.setTotalScore(0);
        paperMapper.insert(paper);
        
        // 添加题目
        if (paperDTO.getQuestions() != null && !paperDTO.getQuestions().isEmpty()) {
            int totalScore = 0;
            for (PaperDTO.PaperQuestionItem item : paperDTO.getQuestions()) {
                PaperQuestion pq = new PaperQuestion();
                pq.setPaperId(paper.getId());
                pq.setQuestionId(item.getQuestionId());
                pq.setScore(item.getScore());
                pq.setSort(item.getSort());
                paperQuestionMapper.insert(pq);
                totalScore += item.getScore();
            }
            paper.setTotalScore(totalScore);
            paperMapper.updateById(paper);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaper(PaperDTO paperDTO) {
        Paper paper = paperMapper.selectById(paperDTO.getId());
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }
        BeanUtil.copyProperties(paperDTO, paper, "id", "totalScore");
        
        // 删除旧题目
        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, paper.getId());
        paperQuestionMapper.delete(wrapper);
        
        // 添加新题目
        if (paperDTO.getQuestions() != null && !paperDTO.getQuestions().isEmpty()) {
            int totalScore = 0;
            for (PaperDTO.PaperQuestionItem item : paperDTO.getQuestions()) {
                PaperQuestion pq = new PaperQuestion();
                pq.setPaperId(paper.getId());
                pq.setQuestionId(item.getQuestionId());
                pq.setScore(item.getScore());
                pq.setSort(item.getSort());
                paperQuestionMapper.insert(pq);
                totalScore += item.getScore();
            }
            paper.setTotalScore(totalScore);
        }
        paperMapper.updateById(paper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaper(Long id) {
        paperMapper.deleteById(id);
        LambdaQueryWrapper<PaperQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaperQuestion::getPaperId, id);
        paperQuestionMapper.delete(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addQuestionsToPaper(Long paperId, PaperDTO.PaperQuestionItem[] questions) {
        Paper paper = paperMapper.selectById(paperId);
        if (paper == null) {
            throw new BusinessException("试卷不存在");
        }
        
        int totalScore = paper.getTotalScore();
        for (PaperDTO.PaperQuestionItem item : questions) {
            PaperQuestion pq = new PaperQuestion();
            pq.setPaperId(paperId);
            pq.setQuestionId(item.getQuestionId());
            pq.setScore(item.getScore());
            pq.setSort(item.getSort());
            paperQuestionMapper.insert(pq);
            totalScore += item.getScore();
        }
        
        paper.setTotalScore(totalScore);
        paperMapper.updateById(paper);
    }
}
