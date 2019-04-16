package com.yyf.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Course;
import com.yyf.entity.Relevance;
import com.yyf.entity.Term;
import com.yyf.entity.User;
import com.yyf.service.CheckingInService;
import com.yyf.service.CourseService;
import com.yyf.service.TermService;
import com.yyf.service.UserService;
import com.yyf.util.ItdragonUtils;
import com.yyf.util.Result;
import com.yyf.util.ResultResponse;
import com.yyf.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈〉<br>
 *
 * @author
 * @create 2019/1/16 16:00
 * @since 1.0.0
 */
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private CheckingInService checkingInService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private TermService termService;

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView enIndex(ModelAndView mv) {
        mv.setViewName("course/index");
        return mv;
    }

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/stu_cource.do")
    public ModelAndView stu_cource(ModelAndView mv) {
        mv.setViewName("course/student/selectedCourse");
        return mv;
    }

    @RequestMapping("/details.do")
    public ModelAndView details(ModelAndView mv) {
        mv.setViewName("course/student/getCourseDetail");
        return mv;
    }

    /**
     * 课程详细，查询所有未选课程
     * @param course
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/get_detail.do")
    public TableResultResponse getDetail(Course course, int page, int limit){
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Course> pageInfo = courseService.getAllCourse(course,page,limit);
        for (Course record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id",   record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("room", record.getRoom());
            resultMap.put("week", record.getWeek());
            resultMap.put("term", record.getTerm());
            resultMap.put("userId", record.getUserId());
            resultMap.put("teacherName",record.getTeacherName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(11, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }


    /**
     * 学生已选课程
     * @param course
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("/getcourse.do")
    public TableResultResponse getstudentCourse(Course course, int page, int limit){
        List<Map<String, Object>> infoList = new ArrayList<>();
        String studentId = itdragonUtils.getSessionUser().getId();
        course.setStudentId(studentId);
        Page<Course> pageInfo = courseService.getCourseByStudentId(course,page,limit);
        for (Course record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id",   record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("room", record.getRoom());
            resultMap.put("week", record.getWeek());
            resultMap.put("term", record.getTerm());
            resultMap.put("userId", record.getUserId());
            resultMap.put("teacherName",record.getTeacherName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(11, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     *  获取学期跳转界面
     * @param
     * @return
     */
    @RequestMapping("/term.do")
    @ResponseBody
    public List<Term> editEnIndex() {
        List<Term> termList = termService.getAllTerm();
        return termList;
    }

    /**
     * 管理界面列表
     *
     * @param course
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("course.do")
    public TableResultResponse enlistmentTables(Course course, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        course.setUserId(itdragonUtils.getSessionUser().getId());
        Page<Course> pageInfo = courseService.selectPage(course, page, limit);
        for (Course record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id",   record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("room", record.getRoom());
            resultMap.put("week", record.getWeek());
            resultMap.put("userId", record.getUserId());
            resultMap.put("state",record.getState());
            resultMap.put("term", record.getTerm());
            //学生集合
            List<User> userList = courseService.getListByUserId(record.getId());
            String users = "";
            for (User user : userList) {
                //返回带样式的users集合
                users = users + "&nbsp;&nbsp; <span onclick=\"delStudent('" + user.getId() + "','" + record.getId() + "')\" style=\"color: #1e9fff\">" + user.getUserName() + "</span>";
            }
            resultMap.put("users", users);
            resultMap.put("userName", userService.selectByPrimaryKey(record.getUserId()).getUserName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(11, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/add.do")
    public ModelAndView addIndex(ModelAndView mv) {
        mv.setViewName("course/add");
        return mv;
    }

    /**
     * 新增
     *
     * @param course
     * @return
     */
    @ResponseBody
    @PostMapping("/course.do")
    public ResultResponse add(Course course) {

        boolean result = courseService.insert(course);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/course.do")
    public ResultResponse delEn(String id) {
        boolean result = courseService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }


    /**
     * 修改跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/edit.do")
    public ModelAndView editEnIndex(ModelAndView mv, String id) {
        Course course = courseService.getOneById(id);
        mv.addObject("course", course);
        mv.setViewName("course/edit");
        return mv;
    }

    /**
     * 修改跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addStudent.do")
    public ModelAndView addStudent(ModelAndView mv,String id,String userId) {
        mv.addObject("id", id);
        mv.addObject("userId", userId);
        mv.setViewName("course/teacher/addStudent");
        return mv;

     /*List<User> userList = courseService.getListNoUserByCourseId(id);
        mv.addObject("userList", userList);*/
    }

    /**
     * 查看选课学生跳转界面

     * @param mv
     * @return
     */

    @RequestMapping("/lookStudent.do")
    public ModelAndView lookStudent(ModelAndView mv, String id) {
        mv.addObject("courseId", id);
        List<User> userList = courseService.getUserBySelectedCourse(id);
        mv.addObject("userList", userList);
        mv.setViewName("course/teacher/lookStudent");
        return mv;
    }

    /**
     * 修改
     *
     * @param course
     * @return
     */
    @ResponseBody
    @PutMapping("/course.do")
    public ResultResponse edit(Course course) {
        course.setTime("2000-10-10 " + course.getTime());
        boolean result = courseService.updateById(course);
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
        return Result.resuleSuccess();
    }

    /**
     * 关闭签到
     *
     * @param course
     * @return
     */
    @ResponseBody
    @PutMapping("/close.do")
    public ResultResponse close(Course course) {
        boolean result = courseService.updateById(course);
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
        checkingInService.delByCourseId(course.getId());
        return Result.resuleSuccess();
    }

    /**
     * 新增
     *
     * @param relevance
     * @return
     */

    @PostMapping("/student.do")
    @ResponseBody
    public ResultResponse student(Relevance relevance) {
        String[] split = relevance.getUserId().split(",");
        boolean result = false;
        boolean flag;

        for (String s : split) {
            Relevance relevance1 = new Relevance();
            relevance1.setUserId(s);
            relevance1.setCourseId(relevance.getCourseId());
            relevance1.setTeacherId(relevance.getTeacherId());

            flag = courseService.selectCourseByUserId(relevance1);
            if(flag){
                result = courseService.insertRelevan(relevance1);
            }else{
                continue;
            }
            relevance1.setId(null);
        }
        if (!result) {
            return Result.resuleError("添加失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除
     *
     * @param studentId
     * @return
     */
    @ResponseBody
    @DeleteMapping("/student.do")
    public ResultResponse delStudent(String studentId, String courseId) {
        boolean result = courseService.delRelevanByUserId(studentId, courseId);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/open.do")
    public ResultResponse open(String id) {
        Course course = new Course();
        course.setId(id);
        course.setState("开启");
        String yard = (int) ((Math.random() * 9 + 1) * 100000) + "";
        course.setYard(yard);
        boolean result = courseService.updateById(course);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess(null, "本次签到码" + yard);
    }

    /**
     * 缺勤名单
     *
     * @param mv
     * @return
     */
    @RequestMapping("/view.do")
    public ModelAndView view(ModelAndView mv, String id) {
        List<User> userList = courseService.absence(id);
        mv.addObject("userList", userList);
        mv.setViewName("course/view");
        return mv;
    }

    /**
     * 学生添加课程(未完成检验表中是否有数据)
     * @param relevance
     * @return
     */
    @RequestMapping("/add_course.do")
    @ResponseBody
    public ResultResponse addCourse(Relevance relevance){

        boolean bool;
        //获取学生信息
        String studentId = itdragonUtils.getSessionUser().getId();
        relevance.setUserId(studentId);
        bool = courseService.insertRelevan(relevance);
        if(!bool){
            return Result.resuleError("添加失败");
        }
        return Result.resuleSuccess();
    }


}