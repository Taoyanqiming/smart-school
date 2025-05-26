package com.sky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 校验过滤器：验证请求中的 Token，并将用户信息存入请求属性
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtProperties jwtProperties; // JWT 配置（密钥、过期时间等）

    // 白名单路径（无需校验 Token 的接口）
    private static final String[] WHITE_LIST = {
            "/user/login", // 登录接口放行
            "/doc.html",   // Swagger 文档
            "/webjars/**",
            "/v2/api-docs",
            "/swagger-resources/**"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. 判断是否在白名单中
        if (isWhiteList(path)) {
            return chain.filter(exchange); // 白名单路径直接放行
        }

        // 2. 从请求头中获取 Token
        String token = extractToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "缺少 Token，未授权");
        }

        try {
            // 3. 解析 JWT Token
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);

            // 4. 提取用户信息（userId 和 role）
            String userId = claims.get(JwtClaimsConstant.USER_ID, String.class);
            String role = claims.get(JwtClaimsConstant.USER_ROLE, String.class);

            if (userId == null || role == null) {
                exchange.getAttributes().put("userId", userId); // 此处存入的属性名与 userKeyResolver 中获取的一致
                exchange.getAttributes().put("role", role);
                return unauthorizedResponse(exchange, "Token 无效，缺少必要信息");
            }

            // 5. 将用户信息存入请求属性（attributes）
            exchange.getAttributes().put("userId", userId);
            exchange.getAttributes().put("role", role);

            // 6. 可选项：将用户信息添加到请求头，传递给下游服务
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .build();

            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

            return chain.filter(newExchange);
        } catch (Exception e) {
            // Token 解析失败（过期、签名错误等）
            return unauthorizedResponse(exchange, "Token 无效：" + e.getMessage());
        }
    }

    @Override
    public int getOrder() {
        return 0; // 过滤器执行顺序，值越小越先执行
    }

    // 从请求头中提取 Token
    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // 去掉 "Bearer " 前缀
        }
        return null;
    }

    // 判断路径是否在白名单中
    private boolean isWhiteList(String path) {
        for (String whitePath : WHITE_LIST) {
            if (path.startsWith(whitePath)) {
                return true;
            }
        }
        return false;
    }

    // 返回未授权响应
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建标准错误响应
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("code", HttpStatus.UNAUTHORIZED.value());
        errorBody.put("message", message);
        errorBody.put("timestamp", System.currentTimeMillis());

        // 使用预配置的 ObjectWriter（线程安全）
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();

        try {
            // 尝试序列化错误响应
            byte[] jsonBytes = writer.writeValueAsBytes(errorBody);
            DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            // 记录错误（实际项目中应使用日志框架）
            System.err.println("Failed to serialize error response: " + e.getMessage());

            // 回退方案：返回简单的纯文本错误
            response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
            String plainError = "Unauthorized: " + message;
            DataBuffer plainBuffer = response.bufferFactory().wrap(plainError.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(plainBuffer));
        }
    }
}