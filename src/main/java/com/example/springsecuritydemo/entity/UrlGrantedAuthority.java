package com.example.springsecuritydemo.entity;

import org.springframework.security.core.GrantedAuthority;

public class UrlGrantedAuthority implements GrantedAuthority {
    private String url;
    private String name;

    public UrlGrantedAuthority(String url, String name) {
        this.url = url;
        this.name = name;
    }
    @Override
    public String getAuthority() {
        return this.url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setuRL(String url) {
        this.url = url;
    }
}
