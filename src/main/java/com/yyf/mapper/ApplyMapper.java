package com.yyf.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yyf.entity.Apply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
/**
 * 角色数据访问层
 */
public interface ApplyMapper extends BaseMapper<Apply> {
    List<Apply> selectApply(RowBounds var1,
                            @Param("departmentId") String departmentId, @Param("status") Integer status);
}
