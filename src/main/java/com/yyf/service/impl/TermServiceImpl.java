package com.yyf.service.impl;

import com.yyf.entity.Term;
import com.yyf.mapper.TermMapper;
import com.yyf.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TermServiceImpl implements TermService {

    @Autowired
    private TermMapper termMapper;

    @Override
    public List<Term> getAllTerm() {
        return termMapper.gettAll();
    }
}
