package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("appoint模块开始创建redis模板对象...");

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        // 设置 Redis 连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置键（Key）的序列化器为 StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置值（Value）的序列化器为 StringRedisSerializer
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // 设置哈希键（Hash Key）和值（Hash Value）的序列化器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}