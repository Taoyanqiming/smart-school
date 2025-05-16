package com.sky.rabbitmq;

import com.sky.dto.CommentDTO;
import com.sky.dto.LikeCommentDTO;
import com.sky.dto.LikeDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 发送评论消息
    public void sendMessage(CommentDTO commentDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DIRECT_EXCHANGE,
                RabbitMQConfig.COMMENT_ROUTING_KEY,
                commentDTO
        );
    }

    // 发送帖子点赞消息
    public void sendMessage(LikeDTO likeDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DIRECT_EXCHANGE,
                RabbitMQConfig.POST_LIKE_ROUTING_KEY,
                likeDTO
        );
    }

    // 发送评论点赞消息
    public void sendMessage(LikeCommentDTO likeCommentDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DIRECT_EXCHANGE,
                RabbitMQConfig.COMMENT_LIKE_ROUTING_KEY,
                likeCommentDTO
        );
    }
}