package com.yyf.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yyf.entity.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 数据访问层
 */
public interface CourseMapper extends BaseMapper<Course> {

}
