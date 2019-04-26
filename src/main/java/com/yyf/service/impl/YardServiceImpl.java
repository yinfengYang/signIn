package com.yyf.service.impl;

import com.yyf.entity.Course;
import com.yyf.entity.Yard;
import com.yyf.mapper.YardMapper;
import com.yyf.service.YardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class YardServiceImpl implements YardService {

    @Autowired
    private YardMapper yardMapper;

    @Override
    public Integer insertYard(String yardCode) {
        Yard yard = new Yard();
        yard.setYard(yardCode);
        Integer result = yardMapper.insertYard(yard);
        if(result > 0 && yard.getId() != null){
            return yard.getId();
        }
        return null;
    }

    @Override
    public Boolean deleteYard(Course course) {
        Integer result = yardMapper.deleteYard(course);
        if(result > 0){
            return true;
        }
        return false;
    }
}
