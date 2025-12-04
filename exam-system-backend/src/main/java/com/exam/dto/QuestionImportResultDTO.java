package com.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目导入结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionImportResultDTO {

    /**
     * 总行数
     */
    private Integer totalRows;

    /**
     * 成功导入数量
     */
    private Integer successCount;

    /**
     * 失败数量
     */
    private Integer failureCount;

    /**
     * 错误信息列表
     */
    @Builder.Default
    private List<String> errorMessages = new ArrayList<>();

    /**
     * 添加错误信息
     */
    public void addErrorMessage(String message) {
        if (errorMessages == null) {
            errorMessages = new ArrayList<>();
        }
        errorMessages.add(message);
    }
}
