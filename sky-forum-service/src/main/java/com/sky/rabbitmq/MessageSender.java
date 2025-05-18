package com.sky.rabbitmq;

import com.sky.dto.CommentDTO;
import com.sky.dto.LikeCommentDTO;
import com.sky.dto.LikeDTO;
import com.sky.dto.MessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sky.utils.RabbitMQConfig;
@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 发送评论消息
    public void sendCommentMessage(MessageDTO messageDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MESSAGE_EXCHANGE,
                "comment.success",  // 路由键
                messageDTO  // 消息内容
        );
    }

    // 发送帖子点赞消息
    public void sendLikeMessage(MessageDTO messageDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MESSAGE_EXCHANGE,
                "like.success",  // 路由键
                messageDTO  // 消息内容
        );
    }

    // 发送评论点赞消息
    public void sendComLikeMessage(MessageDTO messageDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.MESSAGE_EXCHANGE,
                "commentLike.success",  // 路由键
                messageDTO  // 消息内容
        );
    }
}