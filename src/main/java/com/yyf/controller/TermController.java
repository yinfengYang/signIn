package com.yyf.controller;

import com.yyf.entity.Term;
import com.yyf.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/term")
@Controller
public class TermController {

    @Autowired
    private TermService termService;



}
