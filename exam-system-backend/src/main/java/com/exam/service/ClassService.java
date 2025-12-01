package com.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.dto.ClassDTO;
import com.exam.vo.ClassVO;

import java.util.List;

/**
 * 班级Service接口
 */
public interface ClassService {

    /**
     * 分页查询班级列表
     */
    Page<ClassVO> getClassList(Integer current, Integer size, String keyword);

    /**
     * 获取所有班级（不分页）
     */
    List<ClassVO> getAllClasses();

    /**
     * 创建班级
     */
    void createClass(ClassDTO classDTO);

    /**
     * 更新班级
     */
    void updateClass(ClassDTO classDTO);

    /**
     * 删除班级
     */
    void deleteClass(Long id);

    /**
     * 添加学生到班级
     */
    void addStudentsToClass(Long classId, List<Long> studentIds);

    /**
     * 从班级移除学生
     */
    void removeStudentsFromClass(Long classId, List<Long> studentIds);

    /**
     * 获取学生所在的班级
     */
    List<ClassVO> getMyClass(Long studentId);
}
