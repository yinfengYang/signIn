package com.yyf.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.*;
import com.yyf.service.*;
import com.yyf.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

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
    @Autowired
    private YardService yardService;

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
     * 已选课程跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/stu_cource.do")
    public ModelAndView stu_cource(ModelAndView mv) {
        mv.setViewName("course/student/selectedCourse");
        return mv;
    }
    /**
     * 新增课程页面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addCourse.do")
    public ModelAndView add_cource(ModelAndView mv) {
        mv.setViewName("course/teacher/addCourse");
        return mv;
    }

    /**
     * 课程详细页面跳转
     * @param mv
     * @return
     */
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
            resultMap.put("time", record.getTime() == null ? "" : record.getTime());
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
            resultMap.put("time", record.getTime() == null ? "" : record.getTime());
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
     *  获取当前教师对应的课程
     * @param
     * @return
     */
    @RequestMapping("/name.do")
    @ResponseBody
    public List<Course> getTeaIndex(Course course) {
        List<Course> courseList = courseService.getCourseByTeacherId(course);
        return courseList;
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
            if(record.getTime()!= null && record.getWeek() != null){
                Map<String, Object> resultMap = new HashMap<>(16);
                resultMap.put("id",   record.getId());
                resultMap.put("name", record.getName());
                resultMap.put("room", record.getRoom());
                resultMap.put("week", record.getWeek());
                resultMap.put("userId", record.getUserId());
                resultMap.put("state",record.getState());
                resultMap.put("term", record.getTerm());
                resultMap.put("userName", userService.selectByPrimaryKey(record.getUserId()).getUserName());
                resultMap.put("time", record.getTime() == null ? "" : record.getTime());
              //  resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(11, 19));
                infoList.add(resultMap);
            }
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

        boolean flag = courseService.selectCourse(course);
        if(!flag){
            return Result.resuleError("课程重复添加");
        }
        boolean result = courseService.insert(course);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 更新
     *
     * @param course
     * @return
     */
    @ResponseBody
    @PostMapping("/update.do")
    public ResultResponse update(Course course) {

       String courseId =  courseService.selectCourseId(course);
       course.setId(courseId);
        course.setFlag(1);
        boolean result = courseService.updateById(course);
        if (!result) {
            return Result.resuleError("添加课程失败");
        }
        return Result.resuleSuccess(course,"添加课程成功！");
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
     * 移除当前课程
     */
    @ResponseBody
    @PostMapping("/removeStudent.do")
    public ResultResponse removeCourse(String courseId){
        boolean bool = false;
        /*
        * 根据courseID 把 flag 置为 0
        * */
       bool =  courseService.updateFlagByCourseId(courseId);
       if(bool){
        return Result.resuleSuccess();
       }
        return Result.resuleError("移除失败");
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
        boolean bool = false;
        String msg = null;
        //删除签到码表中应的数据
        bool = yardService.deleteYard(course);
        if(bool){
            msg = "已清理本次签到码";
        }
        boolean result = courseService.updateById(course);
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
       // checkingInService.delByCourseId(course.getId()); 删除签到表的对应课程签到信息
        return Result.resuleSuccess(null,"关闭签到 ，"+msg);
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
     * 开启签到
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/open.do")
    public ResultResponse open(Course course) {
        course.setState(1);
        String yardCode = RandomCodeUtil.randomCode();
        Integer yardId = yardService.insertYard(yardCode);
        course.setYardId(yardId);
        boolean result = courseService.updateById(course);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess(null, "本次签到码" + yardCode);
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
     * 学生添加课程
     * @param relevance
     * @return
     */
    @RequestMapping("/add_course.do")
    @ResponseBody
    public ResultResponse addCourse(Relevance relevance){

        boolean bool;
        boolean flag;
        //获取学生信息
        String studentId = itdragonUtils.getSessionUser().getId();
        relevance.setUserId(studentId);
        flag = courseService.countStudentWithCourseFromRelevance(relevance);
        if(flag){
            bool = courseService.insertRelevan(relevance);
            if(!bool){
                return Result.resuleError("添加失败");
            }
            return Result.resuleSuccess();
        }

        return Result.resuleError("已经选过该课程，请不要重复选择");
    }


}