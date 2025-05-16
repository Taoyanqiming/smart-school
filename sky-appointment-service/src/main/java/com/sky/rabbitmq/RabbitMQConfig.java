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

    public static final String ORDER_QUEUE_NAME = "order_queue";
    public static final String MESSAGE_QUEUE_NAME = "message_queue";

    public static final String EXCHANGE_NAME = "direct_exchange";

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE_NAME, true); // 消息持久化
    }

    @Bean
    public Queue messageQueue() {
        return new Queue(MESSAGE_QUEUE_NAME, true); // 消息持久化
    }



    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false); // 交换机持久化
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(directExchange()).with("order");
    }

    @Bean
    public Binding messageBinding() {
        return BindingBuilder.bind(messageQueue()).to(directExchange()).with("message");
    }



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

    // 添加自定义消息转换器
    @Bean
    public Jackson2JsonMessageConverter customMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        // 不设置默认类型，让它自动处理消息类型
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setJavaTypeMapper(javaTypeMapper);
        return converter;
    }
}