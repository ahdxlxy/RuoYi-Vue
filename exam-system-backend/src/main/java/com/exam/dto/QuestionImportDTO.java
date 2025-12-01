package com.exam.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class QuestionImportDTO {

    @ExcelProperty("题型")
    private String type;

    @ExcelProperty("科目")
    private String subject;

    @ExcelProperty("题目内容")
    private String content;

    @ExcelProperty("选项A")
    private String optionA;

    @ExcelProperty("选项B")
    private String optionB;

    @ExcelProperty("选项C")
    private String optionC;

    @ExcelProperty("选项D")
    private String optionD;

    @ExcelProperty("答案")
    private String answer;

    @ExcelProperty("难度")
    private String difficulty;
}
