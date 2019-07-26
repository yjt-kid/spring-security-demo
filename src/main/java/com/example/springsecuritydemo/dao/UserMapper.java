package com.example.springsecuritydemo.dao;

import com.example.springsecuritydemo.entity.SelfUserDetails;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: yjt
 * @description: 用户dao层
 */
@Component
public interface UserMapper {

    //通过username查询用户
    SelfUserDetails getUser(@Param("username") String username);
    List<SelfUserDetails> getAllUser();
}
