package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getTokenName());

        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
            Integer userId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            Integer role = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ROLE).toString());

            BaseContext.setCurrentId(userId);
            BaseContext.setCurrentRole(role); // 设置当前用户角色

            // 根据角色进行权限控制
            // 例如，只有管理员可以访问 /admin 开头的接口
            if (request.getRequestURI().startsWith("/admin") && role != StatusConstant.ADMIN) {
                response.setStatus(403);
                return false;
            }

            return true;
        } catch (Exception ex) {
            response.setStatus(401);
            return false;
        }
    }
}