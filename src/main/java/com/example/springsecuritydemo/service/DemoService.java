package com.example.springsecuritydemo.service;


import com.example.springsecuritydemo.entity.SelfUserDetails;

import java.util.List;

/**
 * @author: yjt
 * @description:
 */
public interface DemoService {

    List<SelfUserDetails> getUser();

}
