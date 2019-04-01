package com.yyf.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Apply;
import com.yyf.entity.CheckingIn;
import com.yyf.entity.Course;
import com.yyf.entity.User;
import com.yyf.service.ApplyService;
import com.yyf.service.CheckingInService;
import com.yyf.service.CourseService;
import com.yyf.service.UserService;
import com.yyf.util.*;
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
@RequestMapping("/checkingIn")
public class CheckingInController {
    @Autowired
    private CheckingInService checkingInService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private ApplyService applyService;

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView enIndex(ModelAndView mv) {
        List<Course> listByType = courseService.getListByType("");
        mv.addObject("courseList", listByType);
        mv.setViewName("checkingIn/index");
        return mv;
    }

    /**
     * 签到页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/signIn.do")
    public ModelAndView signIn(ModelAndView mv) {
        List<Course> listByType = courseService.getCourseListByUserId(itdragonUtils.getSessionUser().getId());
        mv.addObject("courseList", listByType);
        mv.setViewName("checkingIn/signIn");
        return mv;
    }

    /**
     * 新增
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/addSignIn.do")
    public ResultResponse addSignIn(String courseId, String yard) {
        Course course = courseService.getOneById(courseId);
        if (!yard.equals(course.getYard())) {
            return Result.resuleError("签到码错误");
        }
        CheckingIn checkingIn = new CheckingIn();
        checkingIn.setCourseId(courseId);
        checkingIn.setStudentId(itdragonUtils.getSessionUser().getId());
        checkingIn.setStauts("已签到");
        boolean result = checkingInService.insert(checkingIn);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 管理界面列表
     *
     * @param checkingIn
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("checkingIn.do")
    public TableResultResponse enlistmentTables(CheckingIn checkingIn, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        if (userService.isStudent(itdragonUtils.getSessionUser().getId())) {
            checkingIn.setStudentId(itdragonUtils.getSessionUser().getId());
        }
        Page<CheckingIn> pageInfo = checkingInService.selectPage(checkingIn, page, limit);
        for (CheckingIn record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("realName", userService.selectByPrimaryKey(record.getStudentId()).getRealName());
            resultMap.put("courseId", record.getCourseId());
            Course course = courseService.getOneById(record.getCourseId());
            resultMap.put("courseInfo", course.getName() + "-" + course.getRoom() + "-" + course.getWeek() + "-" + course.getTime().substring(11, 19));
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(11, 19));
            resultMap.put("stauts", record.getStauts());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 管理界面列表
     *
     * @param apply
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("applyTables.do")
    public TableResultResponse applyTables(Apply apply, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        if (userService.isStudent(itdragonUtils.getSessionUser().getId())) {
            apply.setUserId(itdragonUtils.getSessionUser().getId());
        }
        Page<Apply> pageInfo = applyService.selectApply(page, limit, apply);
        for (Apply record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("realNam", record.getRealNam());
            resultMap.put("main", record.getMain());
            resultMap.put("status", record.getStatus());
            resultMap.put("createdDate", record.getCreatedDate() == null ? "" : record.getCreatedDate().substring(0, 19));
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
        List<Course> listByType = courseService.getListByType("");
        mv.addObject("courseList", listByType);
        Page<User> userListByRoleId = userService.getUserListByRoleId("5", 1, 1000);
        mv.addObject("userList", userListByRoleId.getRecords());
        mv.setViewName("checkingIn/add");
        return mv;
    }

    /**
     * 跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/leaveHouse.do")
    public ModelAndView leaveHouse(ModelAndView mv) {
        mv.setViewName("checkingIn/leave");
        return mv;
    }

    /**
     * 新增
     *
     * @param apply
     * @return
     */
    @ResponseBody
    @PostMapping("/leave.do")
    public ResultResponse leave(Apply apply) {
        apply.setStatus("待审核");
        apply.setUserId(itdragonUtils.getSessionUser().getId());
        apply.setCreatedDate(DateUtil.getNowDateSS());
        apply.setRealNam(userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId()).getRealName());
        boolean result = applyService.insert(apply);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增
     *
     * @param checkingIn
     * @return
     */
    @ResponseBody
    @PostMapping("/checkingIn.do")
    public ResultResponse add(CheckingIn checkingIn) {
        boolean result = checkingInService.insert(checkingIn);
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
    @DeleteMapping("/checkingIn.do")
    public ResultResponse delEn(String id) {
        boolean result = checkingInService.delById(id);
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
        CheckingIn checkingIn = checkingInService.getOneById(id);
        mv.addObject("checkingIn", checkingIn);
        List<Course> listByType = courseService.getListByType("");
        mv.addObject("courseList", listByType);
        mv.setViewName("checkingIn/edit");
        return mv;
    }

    /**
     * 修改
     *
     * @param checkingIn
     * @return
     */
    @ResponseBody
    @PutMapping("/checkingIn.do")
    public ResultResponse edit(CheckingIn checkingIn) {
        boolean result = checkingInService.updateById(checkingIn);
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
        return Result.resuleSuccess();
    }
}