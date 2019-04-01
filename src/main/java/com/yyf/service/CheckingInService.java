package com.yyf.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.CheckingIn;

import java.util.List;

public interface CheckingInService {
    /**
     * 分页查询
     *
     * @param CheckingIn
     * @param page
     * @param limit
     * @return
     */
    Page<CheckingIn> selectPage(CheckingIn CheckingIn, int page, int limit);

    /**
     * 新增
     *
     * @param CheckingIn
     * @return
     */
    boolean insert(CheckingIn CheckingIn);

    /**
     * 删除
     */
    boolean delById(String id);
    boolean delByCourseId(String courseId);

    /**
     * 修改
     */
    boolean updateById(CheckingIn CheckingIn);

    /**
     * 得到单个对象
     */
    CheckingIn getOneById(String id);

    /**
     * 得到所有集合
     */
    List<CheckingIn> getListByType(String type);

}
