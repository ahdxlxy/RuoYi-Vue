package com.exam.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import cn.hutool.json.JSONUtil;
import com.exam.dto.QuestionDTO;
import com.exam.dto.QuestionImportDTO;
import com.exam.dto.QuestionImportResultDTO;
import com.exam.service.QuestionService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class QuestionImportListener implements ReadListener<QuestionImportDTO> {

    private final QuestionService questionService;
    private final Long teacherId;

    private static final int BATCH_COUNT = 100;
    private List<QuestionDTO> cachedDataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 导入结果统计
     */
    @Getter
    private final QuestionImportResultDTO importResult = QuestionImportResultDTO.builder()
            .totalRows(0)
            .successCount(0)
            .failureCount(0)
            .errorMessages(new ArrayList<>())
            .build();

    private int currentRowNum = 0;

    public QuestionImportListener(QuestionService questionService, Long teacherId) {
        this.questionService = questionService;
        this.teacherId = teacherId;
    }

    @Override
    public void invoke(QuestionImportDTO data, AnalysisContext context) {
        currentRowNum++;
        importResult.setTotalRows(currentRowNum);

        try {
            QuestionDTO questionDTO = convertToQuestionDTO(data, currentRowNum);
            if (questionDTO != null) {
                cachedDataList.add(questionDTO);
            } else {
                // 数据验证失败，跳过该行
                importResult.setFailureCount(importResult.getFailureCount() + 1);
            }
            if (cachedDataList.size() >= BATCH_COUNT) {
                saveData();
                cachedDataList = new ArrayList<>(BATCH_COUNT);
            }
        } catch (Exception e) {
            log.error("处理第{}行数据时发生错误: {}", currentRowNum, e.getMessage());
            importResult.setFailureCount(importResult.getFailureCount() + 1);
            importResult.addErrorMessage(String.format("第%d行: %s", currentRowNum, e.getMessage()));
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！总行数: {}, 成功: {}, 失败: {}",
                importResult.getTotalRows(),
                importResult.getSuccessCount(),
                importResult.getFailureCount());
    }

    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        for (QuestionDTO questionDTO : cachedDataList) {
            try {
                questionService.createQuestion(questionDTO, teacherId);
                importResult.setSuccessCount(importResult.getSuccessCount() + 1);
            } catch (Exception e) {
                log.error("导入题目失败: {}", questionDTO.getContent(), e);
                importResult.setFailureCount(importResult.getFailureCount() + 1);
                String errorMsg = String.format("题目\"%s\"导入失败: %s",
                        questionDTO.getContent() != null && questionDTO.getContent().length() > 20
                                ? questionDTO.getContent().substring(0, 20) + "..."
                                : questionDTO.getContent(),
                        e.getMessage());
                importResult.addErrorMessage(errorMsg);
            }
        }
        log.info("存储数据库完成！");
    }

    private QuestionDTO convertToQuestionDTO(QuestionImportDTO importDTO, int rowNum) {
        // 验证必填字段
        if (importDTO.getContent() == null || importDTO.getContent().trim().isEmpty()) {
            String errorMsg = String.format("第%d行: 题目内容不能为空", rowNum);
            log.warn(errorMsg);
            importResult.addErrorMessage(errorMsg);
            return null;
        }

        if (importDTO.getType() == null || importDTO.getType().trim().isEmpty()) {
            String errorMsg = String.format("第%d行: 题型不能为空", rowNum);
            log.warn(errorMsg);
            importResult.addErrorMessage(errorMsg);
            return null;
        }

        QuestionDTO dto = new QuestionDTO();
        dto.setContent(importDTO.getContent().trim());

        // 科目：可选，默认为空字符串
        dto.setSubject(importDTO.getSubject() != null ? importDTO.getSubject().trim() : "");

        // 答案：可选，默认为空字符串
        dto.setAnswer(importDTO.getAnswer() != null ? importDTO.getAnswer().trim() : "");

        // 转换题型
        String type = importDTO.getType().trim();
        if (type.contains("单选") || type.equalsIgnoreCase("SINGLE"))
            dto.setType("SINGLE");
        else if (type.contains("多选") || type.equalsIgnoreCase("MULTIPLE"))
            dto.setType("MULTIPLE");
        else if (type.contains("判断") || type.equalsIgnoreCase("JUDGE"))
            dto.setType("JUDGE");
        else if (type.contains("填空") || type.equalsIgnoreCase("BLANK"))
            dto.setType("BLANK");
        else if (type.contains("简答") || type.contains("问答") || type.equalsIgnoreCase("ESSAY"))
            dto.setType("ESSAY");
        else {
            // 无法识别的题型，记录警告但使用默认值
            log.warn("第{}行: 无法识别的题型'{}'，使用默认值'单选'", rowNum, type);
            dto.setType("SINGLE");
        }

        // 转换难度：可选，默认为"简单"
        String difficulty = importDTO.getDifficulty();
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            difficulty = difficulty.trim();
            if (difficulty.contains("简单") || difficulty.equalsIgnoreCase("EASY"))
                dto.setDifficulty("EASY");
            else if (difficulty.contains("中等") || difficulty.equalsIgnoreCase("MEDIUM"))
                dto.setDifficulty("MEDIUM");
            else if (difficulty.contains("困难") || difficulty.contains("难") || difficulty.equalsIgnoreCase("HARD"))
                dto.setDifficulty("HARD");
            else {
                log.warn("第{}行: 无法识别的难度'{}'，使用默认值'简单'", rowNum, difficulty);
                dto.setDifficulty("EASY");
            }
        } else {
            dto.setDifficulty("EASY"); // 默认值
        }

        // 处理选项：仅对单选和多选题处理
        if ("SINGLE".equals(dto.getType()) || "MULTIPLE".equals(dto.getType())) {
            Map<String, String> options = new HashMap<>();
            if (importDTO.getOptionA() != null && !importDTO.getOptionA().trim().isEmpty())
                options.put("A", importDTO.getOptionA().trim());
            if (importDTO.getOptionB() != null && !importDTO.getOptionB().trim().isEmpty())
                options.put("B", importDTO.getOptionB().trim());
            if (importDTO.getOptionC() != null && !importDTO.getOptionC().trim().isEmpty())
                options.put("C", importDTO.getOptionC().trim());
            if (importDTO.getOptionD() != null && !importDTO.getOptionD().trim().isEmpty())
                options.put("D", importDTO.getOptionD().trim());

            if (!options.isEmpty()) {
                dto.setOptions(JSONUtil.toJsonStr(options));
            } else {
                // 单选/多选题但没有选项，记录警告
                log.warn("第{}行: {}题缺少选项", rowNum, dto.getType().equals("SINGLE") ? "单选" : "多选");
            }
        }

        return dto;
    }
}
