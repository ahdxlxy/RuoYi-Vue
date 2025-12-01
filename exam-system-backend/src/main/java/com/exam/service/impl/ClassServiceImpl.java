package com.exam.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.common.exception.BusinessException;
import com.exam.dto.ClassDTO;
import com.exam.entity.Class;
import com.exam.entity.StudentClass;
import com.exam.mapper.ClassMapper;
import com.exam.mapper.StudentClassMapper;
import com.exam.service.ClassService;
import com.exam.vo.ClassVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 班级Service实现类
 */
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassMapper classMapper;
    private final StudentClassMapper studentClassMapper;

    @Override
    public Page<ClassVO> getClassList(Integer current, Integer size, String keyword) {
        Page<Class> page = new Page<>(current, size);

        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(keyword), Class::getName, keyword);
        wrapper.orderByDesc(Class::getCreateTime);

        Page<Class> classPage = classMapper.selectPage(page, wrapper);

        // 转换为VO并统计学生数量
        Page<ClassVO> voPage = new Page<>();
        BeanUtil.copyProperties(classPage, voPage, "records");
        List<ClassVO> voList = classPage.getRecords().stream().map(clazz -> {
            ClassVO vo = BeanUtil.copyProperties(clazz, ClassVO.class);
            // 统计学生数量
            LambdaQueryWrapper<StudentClass> scWrapper = new LambdaQueryWrapper<>();
            scWrapper.eq(StudentClass::getClassId, clazz.getId());
            vo.setStudentCount(studentClassMapper.selectCount(scWrapper).intValue());
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public List<ClassVO> getAllClasses() {
        List<Class> classList = classMapper.selectList(null);
        return classList.stream()
                .map(clazz -> BeanUtil.copyProperties(clazz, ClassVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createClass(ClassDTO classDTO) {
        Class clazz = BeanUtil.copyProperties(classDTO, Class.class);
        classMapper.insert(clazz);
    }

    @Override
    public void updateClass(ClassDTO classDTO) {
        Class clazz = classMapper.selectById(classDTO.getId());
        if (clazz == null) {
            throw new BusinessException("班级不存在");
        }
        BeanUtil.copyProperties(classDTO, clazz);
        classMapper.updateById(clazz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteClass(Long id) {
        // 删除班级
        classMapper.deleteById(id);

        // 删除学生班级关联
        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getClassId, id);
        studentClassMapper.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudentsToClass(Long classId, List<Long> studentIds) {
        System.out.println("Adding students to class: classId=" + classId + ", studentIds=" + studentIds);
        for (Long studentId : studentIds) {
            // 检查是否已存在
            LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StudentClass::getClassId, classId);
            wrapper.eq(StudentClass::getStudentId, studentId);
            Long count = studentClassMapper.selectCount(wrapper);
            System.out.println(
                    "Checking existence for student " + studentId + " in class " + classId + ": count=" + count);

            if (count == 0) {
                StudentClass studentClass = new StudentClass();
                studentClass.setClassId(classId);
                studentClass.setStudentId(studentId);
                int rows = studentClassMapper.insert(studentClass);
                System.out.println("Inserted student " + studentId + " into class " + classId + ", rows=" + rows);
            } else {
                System.out.println("Student " + studentId + " already in class " + classId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeStudentsFromClass(Long classId, List<Long> studentIds) {
        for (Long studentId : studentIds) {
            LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StudentClass::getClassId, classId);
            wrapper.eq(StudentClass::getStudentId, studentId);
            studentClassMapper.delete(wrapper);
        }
    }

    @Override
    public List<ClassVO> getMyClass(Long studentId) {
        // 查询学生所在的班级ID
        LambdaQueryWrapper<StudentClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentClass::getStudentId, studentId);
        List<StudentClass> studentClasses = studentClassMapper.selectList(wrapper);

        if (studentClasses.isEmpty()) {
            return List.of();
        }

        List<Long> classIds = studentClasses.stream()
                .map(StudentClass::getClassId)
                .collect(Collectors.toList());

        // 查询班级详情
        List<Class> classes = classMapper.selectBatchIds(classIds);
        return classes.stream()
                .map(clazz -> {
                    ClassVO vo = BeanUtil.copyProperties(clazz, ClassVO.class);
                    // 统计学生数量
                    LambdaQueryWrapper<StudentClass> scWrapper = new LambdaQueryWrapper<>();
                    scWrapper.eq(StudentClass::getClassId, clazz.getId());
                    vo.setStudentCount(studentClassMapper.selectCount(scWrapper).intValue());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
