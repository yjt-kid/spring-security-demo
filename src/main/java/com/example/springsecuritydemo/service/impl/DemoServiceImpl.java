package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.dao.UserMapper;
import com.example.springsecuritydemo.entity.SelfUserDetails;
import com.example.springsecuritydemo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: yjt
 * @description:
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<SelfUserDetails> getUser() {
        return userMapper.getAllUser();
    }
}
