package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.entity.StudentClass;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学生班级关联Mapper
 */
@Mapper
public interface StudentClassMapper extends BaseMapper<StudentClass> {
}
