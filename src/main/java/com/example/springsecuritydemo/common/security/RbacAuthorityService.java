package com.example.springsecuritydemo.common.security;

import com.example.springsecuritydemo.entity.UrlGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: yjt
 * @description: 自定义权限访问控制
 */
@Component("rbacauthorityservice")
public class RbacAuthorityService {
    /**
     *
     * @param request
     * @param authentication
     * @return true有权限
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object userInfo = authentication.getPrincipal();

        boolean hasPermission  = false;

        if (userInfo instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) userInfo;
            List<UrlGrantedAuthority> authorities = (List<UrlGrantedAuthority>) userDetails.getAuthorities();
            //遍历判断是否有权限
            if(authorities != null && !authorities.isEmpty()) {
                for (UrlGrantedAuthority urlGrantedAuthority : authorities) {
                    String url = urlGrantedAuthority.getAuthority();
                    String requestUrl = request.getRequestURI();
                    if(url.equals(requestUrl)) {
                        hasPermission = true;
                        break;
                    }
                }
            }
            return hasPermission;
        } else {
            return false;
        }
    }
}
