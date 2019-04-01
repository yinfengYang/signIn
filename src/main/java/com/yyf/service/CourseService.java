package com.yyf.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Course;
import com.yyf.entity.Relevance;
import com.yyf.entity.User;

import java.util.List;

public interface CourseService {
    /**
     * 分页查询
     *
     * @param Course
     * @param page
     * @param limit
     * @return
     */
    Page<Course> selectPage(Course Course, int page, int limit);

    /**
     * 新增
     *
     * @param Course
     * @return
     */
    boolean insert(Course Course);

    /**
     * 删除
     */
    boolean delById(String id);

    /**
     * 修改
     */
    boolean updateById(Course Course);

    /**
     * 得到单个对象
     */
    Course getOneById(String id);

    /**
     * 得到所有集合
     */
    List<Course> getListByType(String type);


    boolean delRelevanByUserId(String userId,String courseId);


    boolean insertRelevan(Relevance relevance);

    /**
     * 得到该课程下的所有学生
     */
    List<User> getListByUserId(String courseId);

    /**
     * 得到该课程下的未存在所有学生
     * @param courseId
     * @return
     */
    List<User> getListNoUserByCourseId(String courseId);


    /**
     * 得到当前学生报名了的并且开启签到的课程
     */
    List<Course> getCourseListByUserId(String userId);

    /**
     * 查询该课程下缺勤的用户
     */
    List<User> absence(String courseId);
}
