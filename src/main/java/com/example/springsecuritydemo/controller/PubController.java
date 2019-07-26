package com.example.springsecuritydemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pub")
public class PubController {

    @RequestMapping("/hello")
    public String hello(){
        return "不需要登陆即可访问";
    }
}
