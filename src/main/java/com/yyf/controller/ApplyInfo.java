package com.yyf.controller;

import com.yyf.service.ApplyService;
import com.yyf.service.UserService;
import com.yyf.util.ItdragonUtils;
import com.yyf.util.Result;
import com.yyf.util.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author
 * @create 2018/10/29 16:20
 * @since 1.0.0
 */
@Controller
@RequestMapping("/applyInfo")
public class ApplyInfo {
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserService userService;

    /**
     * 主页跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/applyInfo.do")
    public ModelAndView applyInfoHouse(ModelAndView mv) {
        mv.setViewName("/applyInfo/applyInfo");
        return mv;
    }

    /**
     * 主页跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/applyInfoAll")
    public ModelAndView applyInfoAll(ModelAndView mv) {
        mv.addObject("sign", "all");
        mv.addObject("status", 1);
        mv.setViewName("/applyInfo/applyInfo");
        return mv;
    }


    /**
     * 更改状态
     *
     * @param id
     * @param status
     * @return
     */
    @ResponseBody
    @PostMapping("status.do")
    public ResultResponse updateUserStatus(String id, String status) {
        boolean result = applyService.updateStatus(id, status);
        if (!result) {
            return Result.resuleError("更改失败");
        }
        return Result.resuleSuccess();
    }

}