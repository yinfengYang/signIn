package com.yyf.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yyf.entity.Role;
import com.yyf.entity.Yard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色数据访问层
 */
@Mapper
public interface YardMapper extends BaseMapper<Yard> {

    /**
     * 插入签到码，返回主键
     * @param yard
     * @return
     */
    Integer insertYard(Yard yard);
}
