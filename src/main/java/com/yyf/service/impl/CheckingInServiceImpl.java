package com.yyf.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.CheckingIn;
import com.yyf.mapper.CheckingInMapper;
import com.yyf.service.CheckingInService;
import com.yyf.util.DateUtil;
import com.yyf.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author 王钰尧
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class CheckingInServiceImpl implements CheckingInService {
    @Resource
    private CheckingInMapper CheckingInMapper;

    @Override
    public Page<CheckingIn> selectPage(CheckingIn CheckingIn, int page, int limit) {
        EntityWrapper<CheckingIn> searchInfo = new EntityWrapper<>();
        Page<CheckingIn> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(CheckingIn.getStauts())) {
            searchInfo.like("status", CheckingIn.getStauts());
        }
        if (ItdragonUtils.stringIsNotBlack(CheckingIn.getCourseId())) {
            searchInfo.eq("courseId", CheckingIn.getCourseId());
        }
        if (ItdragonUtils.stringIsNotBlack(CheckingIn.getStudentId())) {
            searchInfo.eq("studentId", CheckingIn.getStudentId());
        }
        searchInfo.orderBy("time desc");
        List<CheckingIn> resultList = CheckingInMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(CheckingIn CheckingIn) {
        CheckingIn.setTime(DateUtil.getNowDateSS());
        Integer insert = CheckingInMapper.insert(CheckingIn);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = CheckingInMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delByCourseId(String courseId) {
        EntityWrapper<CheckingIn> wrapper = new EntityWrapper<>();
        wrapper.eq("courseId", courseId);
        Integer delete = CheckingInMapper.delete(wrapper);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(CheckingIn CheckingIn) {
        Integer update = CheckingInMapper.updateById(CheckingIn);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public CheckingIn getOneById(String id) {
        return CheckingInMapper.selectById(id);
    }

    @Override
    public List<CheckingIn> getListByType(String type) {
        EntityWrapper<CheckingIn> searchInfo = new EntityWrapper<>();
        return CheckingInMapper.selectList(searchInfo);
    }
}