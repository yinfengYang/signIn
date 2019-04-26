package com.yyf.service;

import com.yyf.entity.Course;
import com.yyf.entity.Yard;

public interface YardService {

    Integer insertYard(String yard);

    /**
     * 通过课程id 去查询关联yard表的数据进行关闭签到后删除数据
     * @param course
     * @return
     */
    Boolean deleteYard(Course course);

}
