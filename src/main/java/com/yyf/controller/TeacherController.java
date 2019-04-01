package com.yyf.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.User;
import com.yyf.service.LogService;
import com.yyf.service.RoleService;
import com.yyf.service.UserService;
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
 * 用户接口
 *
 * @author
 * @create 2018/10/9 17:02
 * @since 1.0.0
 */
@RequestMapping("/teacher")
@Controller
public class TeacherController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private RoleService roleService;

    /**
     * 用户管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView userManagerHouse(ModelAndView mv) {
        mv.setViewName("teacher/index");
        return mv;
    }

    /**
     * 用户管理界面列表
     *
     * @param user
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("teacher.do")
    public TableResultResponse userTables(User user, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<User> pageInfo = userService.getUserListByRoleId("4", page, limit);
        for (User userEntity : pageInfo.getRecords()) {
            Map<String, Object> userMap = new HashMap<>(16);
            userMap.put("id", userEntity.getId());
            userMap.put("userName", userEntity.getUserName());
            userMap.put("realName", userEntity.getRealName());
            userMap.put("iphone", userEntity.getIphone());
            userMap.put("createdDate", userEntity.getCreatedDate() == null ? "" : userEntity.getCreatedDate().substring(0, 19));
            userMap.put("updateDate", userEntity.getUpdatedDate() == null ? "" : userEntity.getUpdatedDate().substring(0, 19));
            infoList.add(userMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增用户跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/add.do")
    public ModelAndView addUserHouser(ModelAndView mv) {
        mv.setViewName("teacher/add");
        return mv;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/teacher.do")
    public ResultResponse addUser(User user) {
        User checkUser = userService.getUserByUserName(user.getUserName());
        if (checkUser != null) {
            return Result.resuleError("用户名已存在");
        }
        boolean result = userService.insert(user);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        roleService.allotRole(user.getId(), "4");
        return Result.resuleSuccess();
    }

    /**
     * 删除用户,批量删除,单个删除通用
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/teacher.do")
    public ResultResponse delUser(String ids) {
        boolean result = userService.deleteByPrimaryKey(ids);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 更改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    @ResponseBody
    @PostMapping("status.do")
    public ResultResponse updateUserStatus(String id, Integer status) {
        boolean result = userService.updateUserStatus(id, status);
        if (!result) {
            return Result.resuleError("更改失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 修改用户跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/edit.do")
    public ModelAndView editUserHouser(ModelAndView mv, String id) {
        User teacher = userService.selectByPrimaryKey(id);
        mv.addObject("teacher", teacher);
        mv.setViewName("teacher/edit");
        return mv;
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @ResponseBody
    @PutMapping("/teacher.do")
    public ResultResponse editUser(User user) {
        boolean result = userService.updateByPrimaryKey(user);
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
        logService.insert("修改个人信息");
        return Result.resuleSuccess();
    }
}