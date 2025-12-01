package com.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.dto.PaperDTO;
import com.exam.vo.PaperVO;

/**
 * 试卷Service接口
 */
public interface PaperService {
    
    Page<PaperVO> getPaperList(Integer current, Integer size, String keyword);
    
    PaperVO getPaperById(Long id);
    
    void createPaper(PaperDTO paperDTO);
    
    void updatePaper(PaperDTO paperDTO);
    
    void deletePaper(Long id);
    
    void addQuestionsToPaper(Long paperId, PaperDTO.PaperQuestionItem[] questions);
}
