package com.example.springsecuritydemo.common.filters;

import com.example.springsecuritydemo.common.utils.AccessAddressUtil;
import com.example.springsecuritydemo.common.utils.GsonUtil;
import com.example.springsecuritydemo.common.utils.RedisUtil;
import com.example.springsecuritydemo.entity.SelfUserDetails;
import com.example.springsecuritydemo.entity.UrlGrantedAuthority;
import com.example.springsecuritydemo.service.SelfUserDetailsService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author: yjt
 * @description: OncePerRequestFilter确保在一次请求只通过一次filter，而不需要重复执行
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    SelfUserDetailsService userDetailsService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring("Bearer ".length());

            String id = (String) redisUtil.hget(authToken, "id");
            String username = (String) redisUtil.hget(authToken, "username");
            String password = (String) redisUtil.hget(authToken, "password");
            String authoritiesStr = (String) redisUtil.hget(authToken, "authorities");
            List<UrlGrantedAuthority> authorities = GsonUtil.createGson().fromJson(authoritiesStr,
                    new TypeToken<List<UrlGrantedAuthority>>() {
                    }.getType());

            SelfUserDetails userDetails = null;
            if (id != null) {
                userDetails = new SelfUserDetails(Integer.parseInt(id), username, password, authorities);
            }
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //把UsernamePasswordAuthenticationToken放入SecurityContext，不放入Security会认为未登录
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
