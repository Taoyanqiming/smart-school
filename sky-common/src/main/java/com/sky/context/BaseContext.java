package com.sky.context;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class BaseContext {
    public static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();
    public static final ThreadLocal<String> ROLE_THREAD_LOCAL = new ThreadLocal<>();

    // 从请求头中获取用户信息的过滤器
    public static WebFilter createHeaderFilter() {
        return (exchange, chain) -> {
            try {
                // 从请求头中获取用户ID和角色
                String userIdHeader = exchange.getRequest().getHeaders().getFirst("X-User-Id");
                String roleHeader = exchange.getRequest().getHeaders().getFirst("X-User-Role");

                // 转换为对应类型并存入ThreadLocal
                if (userIdHeader != null) {
                    THREAD_LOCAL.set(Integer.valueOf(userIdHeader));
                }
                if (roleHeader != null) {
                    ROLE_THREAD_LOCAL.set(roleHeader);
                }

                return chain.filter(exchange);
            } finally {
                // 请求完成后清除ThreadLocal，避免内存泄漏
                THREAD_LOCAL.remove();
                ROLE_THREAD_LOCAL.remove();
            }
        };
    }

    // 原有方法兼容
    public static void setCurrentId(Integer id) {
        THREAD_LOCAL.set(id);
    }

    public static Integer getCurrentId() {
        return THREAD_LOCAL.get();
    }

    public static void removeCurrentId() {
        THREAD_LOCAL.remove();
    }

    // 新增获取角色方法
    public static String getCurrentRole() {
        return ROLE_THREAD_LOCAL.get();
    }
}