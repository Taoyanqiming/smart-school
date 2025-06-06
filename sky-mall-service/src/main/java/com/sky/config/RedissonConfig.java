package com.sky.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RedissonConfig {
    @Value("192.168.200.141")
    private String host;
    @Value("6379")
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        //配置
        Config config=new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        //创建对并且返回
        return Redisson.create(config);
    }

}
