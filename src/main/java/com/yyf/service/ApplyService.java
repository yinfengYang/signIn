package com.yyf.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Apply;
import com.yyf.entity.User;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author
 * @create 2018/10/29 16:21
 * @since 1.0.0
 */
public interface ApplyService {

    Page<Apply> selectApply(int page, int limit, Apply apply);


    boolean updateStatus(String id, String status);

    boolean insert(Apply apply);

    boolean updateTomoStatus(String id, Integer status);

    boolean updateUser(User user);
}