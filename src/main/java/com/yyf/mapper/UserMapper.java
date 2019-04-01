package com.yyf.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yyf.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 用户数据访问层
 * 继承与一个Mybatis-plus 中的BaseMapper<T>
 * Mybatis-Plus 是一款 Mybatis 动态 SQL 自动注入 Mybatis 增删改查 CRUD 操作中间件
 * 减少你的开发周期优化动态维护 XML 实体字段。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过roleId查询其下面的所有用户
     *
     * @param roleId
     * @return
     */
    List<User> selectUserListByRoleId(RowBounds var1, @Param(value = "roleId") String roleId);
}
