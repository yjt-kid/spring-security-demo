package com.example.springsecuritydemo.dao;

import com.example.springsecuritydemo.entity.Menu;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MenuMapper {
    List<Menu> getMenu(Integer id);
}
