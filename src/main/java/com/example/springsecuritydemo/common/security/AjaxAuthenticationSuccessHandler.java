package com.example.springsecuritydemo.common.security;

import com.alibaba.fastjson.JSON;
import com.example.springsecuritydemo.common.Enums.ResultEnum;
import com.example.springsecuritydemo.common.utils.JwtTokenUtil;
import com.example.springsecuritydemo.common.utils.RedisUtil;
import com.example.springsecuritydemo.common.vo.ResultVO;
import com.example.springsecuritydemo.entity.SelfUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: yjt
 * @description: 用户登录成功时返回给前端的数据
 */
@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SelfUserDetails userDetails = (SelfUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Map<String,Object> map = new HashMap<>();
        map.put("password",userDetails.getPassword());
        String jwtToken = JwtTokenUtil.generateToken(username, expirationSeconds, map);
        redisUtil.setTokenRefresh(jwtToken,userDetails);
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGIN_SUCCESS,jwtToken,true)));
    }
}
