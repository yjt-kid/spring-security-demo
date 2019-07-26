package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.entity.SelfUserDetails;
import com.example.springsecuritydemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: yjt
 * @description: demo
 */
@RestController
@RequestMapping("/test")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/getUser")
    public List<SelfUserDetails> getUser(){
        List<SelfUserDetails> result = demoService.getUser();
        return result;
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(){
        return "删除成功";
    }


}
