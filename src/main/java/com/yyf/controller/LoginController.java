package com.yyf.controller;

import com.yyf.entity.User;
import com.yyf.service.LogService;
import com.yyf.service.UserService;
import com.yyf.util.DateUtil;
import com.yyf.util.ItdragonUtils;
import com.yyf.util.Result;
import com.yyf.util.ResultResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户登入登出接口
 *
 * @author
 * @create 2018/10/7 15:57
 * @since 1.0.0
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private LogService logService;

    private static final transient Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * 用户登录操作
     *
     * @param userName
     * @return
     */
    @ResponseBody
    @PostMapping("login.do")
    public ResultResponse login(String userName, String password) {
        log.info("^^^^^^^^^^^^^^^^^^^^登录信息:用户名: " + userName + "密码: " + password);
        try {
            //shiro 安全验证，返回一个key值，如果key值为空就创建一个并且绑定返回
            Subject currentUser = SecurityUtils.getSubject();
            log.info("^^^^^^^^^^^^^^^^^^^^  是否已通过认证" + currentUser.isAuthenticated());
            //验证用户是否被禁用
            User user = userService.getUserByUserName(userName);
            if (user == null) {
                return Result.resuleError("用户不存在");
            }
            if (user.getStatus() == 1) {
                if (!currentUser.isAuthenticated()) {
                    log.info("^^^^^^^^^^^^^^^^^^^^执行登录操作");
                    UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
//	            token.setRememberMe(true);
                    try {
                        // 执行登录
                        currentUser.login(token);
                        log.info("^^^^^^^^^^^^^^^^^^^^登录成功");
                        //日志记录
                        //将登陆信息存储到session中
                        User loginUserInfo = new User();
                        //获取当前时间
                        String loginTime = DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
                        log.info("^^^^^^^^^^^^^^^^^^^^登陆时间为" + loginTime);
                        loginUserInfo.setUserName(userName);
                        loginUserInfo.setPlainPassword(password);

                        //获取Shiro session 并且设置属性值和属性，暂时保存登录时的密码
                        itdragonUtils.getShiroSession().setAttribute("loginUserInfo", loginUserInfo);
                        itdragonUtils.getShiroSession().setAttribute("userInfo", user);
                        //修改登陆时间
                        userService.updateLoginTime(loginUserInfo.getUserName(), loginTime);
                        logService.insert("登录操作");
                        return Result.resuleSuccess();
                    } catch (AuthenticationException ae) {
                        log.info("^^^^^^^^^^^^^^^^^^^^ 登录失败,账号密码不匹配: " + ae.getMessage());
                        return Result.resuleError("账号密码不匹配");
                    }
                }
            }
            return Result.resuleError("用户已被禁用;请联系管理员");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.resuleError(e.getMessage());
        }

    }

    /**
     * 登出
     *
     * @param session
     */
    @GetMapping("logout.do")
    public void logout(HttpSession session) {
        try {
            //退出，shrio logout() 删除session 清楚登录状态
            SecurityUtils.getSubject().logout();
            log.info("^^^^^^^^^^^^^^^^^^^^登出成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}