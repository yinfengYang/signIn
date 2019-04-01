package com.yyf.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yyf.entity.Apply;
import com.yyf.entity.User;
import com.yyf.mapper.ApplyMapper;
import com.yyf.mapper.UserMapper;
import com.yyf.service.ApplyService;
import com.yyf.util.ItdragonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author
 * @create 2018/10/29 16:26
 * @since 1.0.0
 */
@Service
public class ApplyInfoServiceImpl implements ApplyService {
    @Resource
    private ApplyMapper applyMapper;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Resource
    private UserMapper userMapper;

    @Override
    public Page<Apply> selectApply(int page, int limit, Apply apply) {
        Page<Apply> pageInfo = new Page<>(page, limit);
        EntityWrapper<Apply> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(apply.getUserId())) {
            wrapper.eq("userId", apply.getUserId());
        }
        List<Apply> applyList = applyMapper.selectPage(pageInfo, wrapper);
        if (!applyList.isEmpty()) {
            pageInfo.setRecords(applyList);
        }
        return pageInfo;
    }


    @Override
    public boolean updateStatus(String id, String status) {
        Apply apply = new Apply();
        apply.setId(id);
        apply.setStatus(status);
        int result = applyMapper.updateById(apply);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(Apply apply) {
        int result = applyMapper.insert(apply);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTomoStatus(String id, Integer status) {
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }


}