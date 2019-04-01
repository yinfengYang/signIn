package com.yyf.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.CheckingIn;
import com.yyf.entity.Course;
import com.yyf.entity.Relevance;
import com.yyf.entity.User;
import com.yyf.mapper.CheckingInMapper;
import com.yyf.mapper.CourseMapper;
import com.yyf.mapper.RelevanceMapper;
import com.yyf.mapper.UserMapper;
import com.yyf.service.CourseService;
import com.yyf.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public Page<Course> selectPage(Course Course, int page, int limit) {
        EntityWrapper<Course> searchInfo = new EntityWrapper<>();
        Page<Course> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(Course.getName())) {
            searchInfo.like("name", Course.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(Course.getRoom())) {
            searchInfo.eq("room", Course.getRoom());
        }
        if (ItdragonUtils.stringIsNotBlack(Course.getWeek())) {
            searchInfo.eq("week", Course.getWeek());
        }
        if (ItdragonUtils.stringIsNotBlack(Course.getUserId())) {
            searchInfo.eq("userId", Course.getUserId());
        }
        searchInfo.orderBy("time desc");
        List<Course> resultList = CourseMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Course Course) {
        Course.setState("关闭");
        Course.setUserId(itdragonUtils.getSessionUser().getId());
        Integer insert = CourseMapper.insert(Course);
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
        Integer delete = relevanceMapper.insert(relevance);
        if (delete > 0) {
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

    @Override
    public List<User> getListNoUserByCourseId(String courseId) {
        //查询出所有学生的集合
        Page<User> pageInfo = new Page<>(1, 1000);
        List<User> userList1 = userMapper.selectUserListByRoleId(pageInfo, "5");
        List<User> userList = new ArrayList<>();
        EntityWrapper<Relevance> wrapper = new EntityWrapper<>();
        wrapper.eq("courseId", courseId);
        //查询出改课程的所有集合
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
}