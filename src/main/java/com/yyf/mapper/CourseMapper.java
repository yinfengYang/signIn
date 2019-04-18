package com.yyf.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Course;
import com.yyf.entity.Relevance;
import com.yyf.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
/**
 * 数据访问层
 */
public interface CourseMapper extends BaseMapper<Course> {

    Integer selectCourseByUserId(Relevance relevance);

    List<User> getUserByCourseId(@Param("courseId") String id);

    /**
     * 通过studentId 获取 课程
     * @return
     */
    List<Course> getCourseByStudentId(RowBounds rowBounds,Course course);

    /**
     * 查询所有课程
     * @param rowBounds
     * @param course
     * @return
     */
    List<Course> selectAll(RowBounds rowBounds,Course course);

    /**
     * 获取当前教师所课程
     * @param course
     * @return
     */
    List<Course> getCourseByTeacherId(Course course);

    Integer selectCourse(Course course);

    String[] getCourseID(Course course);




}
