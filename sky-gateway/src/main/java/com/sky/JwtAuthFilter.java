package com.sky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 校验过滤器：验证请求中的 Token，提取用户信息，并进行路径权限控制
 * JWT Token 校验
 * 用户信息提取
 * 路径权限控制
 * 统一错误响应格式
 * 请求信息传递到下游服务
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtProperties jwtProperties;

    // 路径匹配器
    private final PathMatcher pathMatcher = new AntPathMatcher();

    // 白名单路径（无需校验 Token 的接口）
    private static final String[] WHITE_LIST = {
            "/user/login",
            "/doc.html",
            "/webjars/**",
            "/v2/api-docs",
            "/swagger-resources/**"
    };

    // 权限路径配置（路径模式 -> 允许的角色列表）
    private static final Map<String, String[]> PROTECTED_PATHS = new HashMap<>();
    static {
        PROTECTED_PATHS.put("/admin/**", new String[]{"admin"});
        PROTECTED_PATHS.put("/sys/**", new String[]{"admin", "super"});
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. 判断是否在白名单中
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 2. 从请求头中获取 Token
        String token = extractToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "缺少 Token，未授权");
        }

        try {
            // 3. 解析 JWT Token
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);

            // 4. 提取用户信息
            Integer userId = claims.get(JwtClaimsConstant.USER_ID, Integer.class);
            String role = claims.get(JwtClaimsConstant.USER_ROLE, String.class);

            if (userId == null || role == null) {
                return unauthorizedResponse(exchange, "Token 无效，缺少必要信息");
            }

            // 5. 存储用户信息到请求属性
            exchange.getAttributes().put("userId", userId);
            exchange.getAttributes().put("role", role);

            // 6. 路径权限校验
            if (!hasPermission(path, role)) {
                return forbiddenResponse(exchange, "无权限访问该路径");
            }

            // 7. 继续执行请求链
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Role", role)
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);

        } catch (Exception e) {
            return unauthorizedResponse(exchange, "Token 无效：" + e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return 0; // 过滤器执行顺序
    }

    // 从请求头中提取 Token
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // 判断路径是否在白名单中
    private boolean isWhiteList(String path) {
        for (String whitePath : WHITE_LIST) {
            if (pathMatcher.match(whitePath, path)) {
                return true;
            }
        }
        return false;
    }

    // 检查用户是否有权限访问路径
    private boolean hasPermission(String path, String userRole) {
        for (Map.Entry<String, String[]> entry : PROTECTED_PATHS.entrySet()) {
            String pattern = entry.getKey();
            String[] allowedRoles = entry.getValue();

            if (pathMatcher.match(pattern, path)) {
                for (String role : allowedRoles) {
                    if (role.equals(userRole)) {
                        return true;
                    }
                }
                return false; // 匹配到保护路径但角色不允许
            }
        }
        return true; // 未匹配到保护路径，默认允许访问
    }

    // 返回未授权响应
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        return buildErrorResponse(exchange, HttpStatus.UNAUTHORIZED, message);
    }

    // 返回禁止访问响应
    private Mono<Void> forbiddenResponse(ServerWebExchange exchange, String message) {
        return buildErrorResponse(exchange, HttpStatus.FORBIDDEN, message);
    }

    // 构建统一的错误响应
    private Mono<Void> buildErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("code", status.value());
        errorBody.put("message", message);
        errorBody.put("timestamp", System.currentTimeMillis());

        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] jsonBytes = mapper.writeValueAsBytes(errorBody);
            DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            String plainError = status.getReasonPhrase() + ": " + message;
            DataBuffer plainBuffer = response.bufferFactory().wrap(plainError.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(plainBuffer));
        }
    }
}