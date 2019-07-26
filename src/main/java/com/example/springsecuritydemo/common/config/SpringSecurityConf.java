package com.example.springsecuritydemo.common.config;

import com.example.springsecuritydemo.common.filters.JwtAuthenticationTokenFilter;
import com.example.springsecuritydemo.common.security.*;
import com.example.springsecuritydemo.service.SelfUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置类
 *
 * @author: yjt
 */
@Configuration
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {


    @Autowired
    AjaxAuthenticationEntryPoint authenticationEntryPoint;//未登陆时返回 JSON 格式的数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler; //登录成功返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationFailureHandler authenticationFailureHandler; //登录失败返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxLogoutSuccessHandler logoutSuccessHandler;//注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）

    @Autowired
    AjaxAccessDeniedHandler accessDeniedHandler;//无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）

    @Autowired
    SelfUserDetailsService userDetailsService; // 自定义userDetailsService

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; // JWT 拦截器

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore，设置不需要登录就可以访问
        web.ignoring().antMatchers("/pub/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 去掉 CSRF，防止csrf攻击
        http.csrf().disable()
                // 使用 JWT，关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //设置未登陆时返回
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .authorizeRequests()//定义哪些URL需要被保护、哪些不需要被保护
                .anyRequest()//任何请求,登录后可以访问
                .access("@rbacauthorityservice.hasPermission(request,authentication)") // 动态 url 认证

                .and()
                .formLogin()  //开启登录, 定义当需要用户登录时候，转到的登录页面
//                .loginPage("/login.html")
//                .loginProcessingUrl("/login") //自定义登录url
                .successHandler(authenticationSuccessHandler) // 登录成功
                .failureHandler(authenticationFailureHandler) // 登录失败
                .permitAll()

                .and()
                .logout()//默认注销行为为logout
                //定义注销url
                .logoutUrl("/logout")
                //注销成功处理方法
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler); // 无权访问时处理类
        // JWT Filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
