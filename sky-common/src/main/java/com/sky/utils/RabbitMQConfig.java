package com.sky.utils;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 交换机名称
    public static final String MESSAGE_EXCHANGE = "message.exchange.topic";

    // 队列名称
    public static final String ORDER_QUEUE = "order.message.queue";
    public static final String APPOINTMENT_QUEUE = "appointment.message.queue";
    public static final String LIKE_QUEUE = "like.message.queue";
    public static final String COMMENT_QUEUE = "comment.message.queue";
    public static final String COMMENT_LIKE_QUEUE = "commentLike.message.queue";
    // 路由键
    public static final String ORDER_ROUTING_KEY = "order.#";
    public static final String APPOINTMENT_ROUTING_KEY = "appointment.#";
    public static final String LIKE_ROUTING_KEY = "like.#";
    public static final String COMMENT_ROUTING_KEY = "comment.#";
    public static final String COMMENT_LIKE_ROUTING_KEY = "commentLike.#";
    // 创建主题交换机
    @Bean
    public TopicExchange messageExchange() {
        return new TopicExchange(MESSAGE_EXCHANGE);
    }

    // 创建订单消息队列
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    // 创建预约消息队列
    @Bean
    public Queue appointmentQueue() {
        return new Queue(APPOINTMENT_QUEUE, true);
    }

    // 创建点赞消息队列
    @Bean
    public Queue likeQueue() {
        return new Queue(LIKE_QUEUE, true);
    }

    // 创建评论消息队列
    @Bean
    public Queue commentQueue() {
        return new Queue(COMMENT_QUEUE, true);
    }

    // 创建评论点赞消息队列
    @Bean
    public Queue commentLikeQueue() {
        return new Queue(COMMENT_LIKE_QUEUE, true);
    }

    // 绑定队列到交换机
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(messageExchange()).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding appointmentBinding() {
        return BindingBuilder.bind(appointmentQueue()).to(messageExchange()).with(APPOINTMENT_ROUTING_KEY);
    }

    @Bean
    public Binding likeBinding() {
        return BindingBuilder.bind(likeQueue()).to(messageExchange()).with(LIKE_ROUTING_KEY);
    }

    @Bean
    public Binding commentBinding() {
        return BindingBuilder.bind(commentQueue()).to(messageExchange()).with(COMMENT_ROUTING_KEY);
    }

    @Bean
    public Binding commentLikeBinding() {
        return BindingBuilder.bind(commentLikeQueue()).to(messageExchange()).with(COMMENT_LIKE_ROUTING_KEY);
    }
}