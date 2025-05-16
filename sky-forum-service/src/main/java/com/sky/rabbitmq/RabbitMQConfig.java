package com.sky.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 队列名称常量
    public static final String COMMENT_QUEUE = "comment.queue";
    public static final String POST_LIKE_QUEUE = "post.like.queue";
    public static final String COMMENT_LIKE_QUEUE = "comment.like.queue";

    // 交换机名称常量
    public static final String DIRECT_EXCHANGE = "direct.exchange";

    // 路由键常量
    public static final String COMMENT_ROUTING_KEY = "comment";
    public static final String POST_LIKE_ROUTING_KEY = "post.like";
    public static final String COMMENT_LIKE_ROUTING_KEY = "comment.like";

    // 定义评论队列
    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT_QUEUE, true); // 持久化队列
    }

    // 定义帖子点赞队列
    @Bean
    public Queue postLikeQueue() {
        return new Queue(POST_LIKE_QUEUE, true); // 持久化队列
    }

    // 定义评论点赞队列
    @Bean
    public Queue commentLikeQueue() {
        return new Queue(COMMENT_LIKE_QUEUE, true); // 持久化队列
    }

    // 定义直连交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    // 绑定评论队列到交换机
    @Bean
    public Binding commentBinding() {
        return BindingBuilder.bind(commentQueue()).to(directExchange()).with(COMMENT_ROUTING_KEY);
    }

    // 绑定帖子点赞队列到交换机
    @Bean
    public Binding postLikeBinding() {
        return BindingBuilder.bind(postLikeQueue()).to(directExchange()).with(POST_LIKE_ROUTING_KEY);
    }

    // 绑定评论点赞队列到交换机
    @Bean
    public Binding commentLikeBinding() {
        return BindingBuilder.bind(commentLikeQueue()).to(directExchange()).with(COMMENT_LIKE_ROUTING_KEY);
    }

    // 配置RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(customMessageConverter());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("消息发送成功，correlationData: " + correlationData);
            } else {
                System.err.println("消息发送失败，原因: " + cause);
            }
        });
        return rabbitTemplate;
    }

    // 配置JSON消息转换器
    @Bean
    public Jackson2JsonMessageConverter customMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        // 设置信任的包，确保反序列化安全
        typeMapper.setTrustedPackages("com.sky.dto");

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}