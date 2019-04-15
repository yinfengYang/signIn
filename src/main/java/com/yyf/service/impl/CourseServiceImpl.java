package com.yyf.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.*;
import com.yyf.mapper.CheckingInMapper;
import com.yyf.mapper.CourseMapper;
import com.yyf.mapper.RelevanceMapper;
import com.yyf.mapper.UserMapper;
import com.yyf.service.CourseService;
import com.yyf.util.DateUtil;
import com.yyf.util.ItdragonUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Resource
    private CourseMapper CourseMapper;
    @Resource
    private RelevanceMapper relevanceMapper;
    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CheckingInMapper checkingInMapper;

    @Override
    public Page<Course> selectPage(Course course, int page, int limit) {
        EntityWrapper<Course> searchInfo = new EntityWrapper<>();
        Page<Course> pageInfo = new Page<>(page, limit);
        //mybatis-plus 条件查询实现的一种方法

        if (ItdragonUtils.stringIsNotBlack(course.getName())) {
            //mybatis-plus的模糊查询
            searchInfo.like("name", course.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(course.getTerm())) {
            searchInfo.eq("term", course.getTerm());
        }
        if (ItdragonUtils.stringIsNotBlack(course.getWeek())) {
            searchInfo.eq("week", course.getWeek());
        }
        if (ItdragonUtils.stringIsNotBlack(course.getUserId())) {
            searchInfo.eq("userId", course.getUserId());
        }
        searchInfo.orderBy("time desc");

        //mybatis-plus分页查询
        List<Course> resultList = CourseMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Course course) {


      //  course.setTime(DateUtil.getTimeN()+"-"+ course.getTime());
        course.setState("关闭");
        course.setUserId(itdragonUtils.getSessionUser().getId());
        Integer insert = CourseMapper.insert(course);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = CourseMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(Course Course) {
        Integer update = CourseMapper.updateById(Course);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Course getOneById(String id) {
        return CourseMapper.selectById(id);
    }

    @Override
    public List<Course> getListByType(String type) {
        EntityWrapper<Course> searchInfo = new EntityWrapper<>();
        return CourseMapper.selectList(searchInfo);
    }

    @Override
    public boolean delRelevanByUserId(String userId, String courseId) {
        EntityWrapper<Relevance> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("courseId", courseId);
        Integer delete = relevanceMapper.delete(wrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insertRelevan(Relevance relevance) {
        Integer result = relevanceMapper.insert(relevance);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<User> getListByUserId(String courseId) {
        EntityWrapper<Relevance> wrapper = new EntityWrapper<>();
        wrapper.eq("courseId", courseId);
        List<Relevance> relevanceList = relevanceMapper.selectList(wrapper);
        List<User> userList = new ArrayList<>();
        for (Relevance relevance : relevanceList) {
            User user = userMapper.selectById(relevance.getUserId());
            if (user != null) {
                userList.add(user);
            }

        }
        return userList;
    }

    /**
     * 查询已经选课的学生
     * @param courseId
     * @return
     */
    @Override
    public List<User> getListNoUserByCourseId(String courseId) {

        Page<User> pageInfo = new Page<>(1, 1000);
        User u = new User();
        u.setRoleId("5");

        //查询出所有学生的集合
        List<User> userList1 = userMapper.selectUserListByRoleId(pageInfo, u);
        List<User> userList = new ArrayList<>();

        EntityWrapper<Relevance> wrapper = new EntityWrapper<>();
        wrapper.eq("courseId", courseId);
        //通过courseId 查询出该课程的所有集合
        List<Relevance> relevanceList = relevanceMapper.selectList(wrapper);
        List<String> isHave = new ArrayList<>();
        for (Relevance relevance : relevanceList) {
            isHave.add(relevance.getUserId());
        }
        for (User user : userList1) {
            if (!isHave.contains(user.getId())) {
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public List<Course> getCourseListByUserId(String userId) {
        //查询该学生报名了的课程
        List<Course> resultList = new ArrayList<>();
        EntityWrapper<Relevance> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        List<Relevance> relevanceList = relevanceMapper.selectList(wrapper);
        List<String> sList = new ArrayList<>();
        for (Relevance relevance : relevanceList) {
            sList.add(relevance.getCourseId());
        }
        //得到开启签到的课程
        EntityWrapper<Course> wrapper1 = new EntityWrapper<>();
        wrapper1.eq("state", "开启");
        List<Course> courseList = CourseMapper.selectList(wrapper1);
        for (Course course : courseList) {
            if (sList.contains(course.getId())) {
                resultList.add(course);
            }
        }
        return resultList;
    }

    @Override
    public List<User> absence(String courseId) {
        List<User> resultList = new ArrayList<>();
        //得到该课程下的集合
        EntityWrapper<Relevance> wrapper = new EntityWrapper<>();
        wrapper.eq("courseId", courseId);
        List<Relevance> relevanceList = relevanceMapper.selectList(wrapper);
        List<String> sList = new ArrayList<>();
        for (Relevance relevance : relevanceList) {
            sList.add(relevance.getUserId());
        }
        //得到这个课程下签了到的学生名单
        EntityWrapper<CheckingIn> wrapper1 = new EntityWrapper<>();
        wrapper.eq("courseId", courseId);
        List<CheckingIn> checkingInList = checkingInMapper.selectList(wrapper1);
        List<String> sheaList = new ArrayList<>();
        for (CheckingIn checkingIn : checkingInList) {
            sheaList.add(checkingIn.getStudentId());
        }
        for (String userId : sList) {
            if (!sheaList.contains(userId)) {
                resultList.add(userMapper.selectById(userId));
            }
        }
        return resultList;
    }

    @Override
    public Boolean selectCourseByUserId(Relevance relevance) {

            Integer result = CourseMapper.selectCourseByUserId(relevance);
            if(result > 0){
                return false;
            }

        return true;
    }

    @Override
    public List<User>getUserBySelectedCourse(String courseId) {
        return  CourseMapper.getUserByCourseId(courseId);
    }
}