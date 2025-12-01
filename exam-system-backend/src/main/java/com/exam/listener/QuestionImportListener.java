package com.exam.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import cn.hutool.json.JSONUtil;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionImportDTO;
import com.exam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class QuestionImportListener implements ReadListener<QuestionImportDTO> {

    private final QuestionService questionService;
    private final Long teacherId;

    private static final int BATCH_COUNT = 100;
    private List<QuestionDTO> cachedDataList = new ArrayList<>(BATCH_COUNT);

    @Override
    public void invoke(QuestionImportDTO data, AnalysisContext context) {
        QuestionDTO questionDTO = convertToQuestionDTO(data);
        if (questionDTO != null) {
            cachedDataList.add(questionDTO);
        }
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = new ArrayList<>(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        for (QuestionDTO questionDTO : cachedDataList) {
            try {
                questionService.createQuestion(questionDTO, teacherId);
            } catch (Exception e) {
                log.error("导入题目失败: {}", questionDTO, e);
            }
        }
        log.info("存储数据库成功！");
    }

    private QuestionDTO convertToQuestionDTO(QuestionImportDTO importDTO) {
        if (importDTO.getContent() == null || importDTO.getType() == null) {
            return null;
        }

        QuestionDTO dto = new QuestionDTO();
        dto.setContent(importDTO.getContent());
        dto.setSubject(importDTO.getSubject());
        dto.setAnswer(importDTO.getAnswer());

        // 转换题型
        String type = importDTO.getType();
        if (type.contains("单选"))
            dto.setType("SINGLE");
        else if (type.contains("多选"))
            dto.setType("MULTIPLE");
        else if (type.contains("判断"))
            dto.setType("JUDGE");
        else if (type.contains("填空"))
            dto.setType("BLANK");
        else if (type.contains("简答") || type.contains("问答"))
            dto.setType("ESSAY");
        else
            dto.setType("SINGLE"); // 默认单选

        // 转换难度
        String difficulty = importDTO.getDifficulty();
        if (difficulty != null) {
            if (difficulty.contains("简单"))
                dto.setDifficulty("EASY");
            else if (difficulty.contains("中等"))
                dto.setDifficulty("MEDIUM");
            else if (difficulty.contains("困难"))
                dto.setDifficulty("HARD");
            else
                dto.setDifficulty("EASY");
        } else {
            dto.setDifficulty("EASY");
        }

        // 处理选项
        if ("SINGLE".equals(dto.getType()) || "MULTIPLE".equals(dto.getType())) {
            Map<String, String> options = new HashMap<>();
            if (importDTO.getOptionA() != null)
                options.put("A", importDTO.getOptionA());
            if (importDTO.getOptionB() != null)
                options.put("B", importDTO.getOptionB());
            if (importDTO.getOptionC() != null)
                options.put("C", importDTO.getOptionC());
            if (importDTO.getOptionD() != null)
                options.put("D", importDTO.getOptionD());
            dto.setOptions(JSONUtil.toJsonStr(options));
        }

        return dto;
    }
}
