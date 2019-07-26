package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.dao.MenuMapper;
import com.example.springsecuritydemo.dao.UserMapper;
import com.example.springsecuritydemo.entity.Menu;
import com.example.springsecuritydemo.entity.SelfUserDetails;
import com.example.springsecuritydemo.entity.UrlGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yjt
 * @description: 用户认证、权限
 */
@Component
public class SelfUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过username查询用户
        SelfUserDetails user = userMapper.getUser(username);
        if(user == null){
            throw new UsernameNotFoundException("该用户不存在");
        }
        List<UrlGrantedAuthority> authoritiesList = new ArrayList<>();
        //查询用户菜单
        List<Menu> menuList = menuMapper.getMenu(user.getId());
        if (menuList != null && !menuList.isEmpty()) {
            for (Menu menu:menuList
                 ) {
                UrlGrantedAuthority grantedAuthority = new UrlGrantedAuthority(menu.getMenuUrl(),menu.getMenuName());
                authoritiesList.add(grantedAuthority);
            }
        }
        user.setAuthorities(authoritiesList);
        return user;
    }
}
