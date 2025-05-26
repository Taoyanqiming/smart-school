package com.sky;

import com.sky.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.server.WebFilter;

@EnableTransactionManagement //开启注解方式的事务管理
@Slf4j
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class AppointApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointApplication.class, args);
        log.info("server started");
    }
    // 注册过滤器，将请求头信息存入ThreadLocal
    @Bean
    public WebFilter baseContextFilter() {
        return BaseContext.createHeaderFilter();
    }
}
