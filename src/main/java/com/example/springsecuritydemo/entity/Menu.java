package com.example.springsecuritydemo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜单表
 */
@Data
public class Menu implements Serializable {
    private Integer id;
    private String menuUrl;
    private String menuName;
}
