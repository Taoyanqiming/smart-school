package com.sky.service.impl;

import com.sky.dto.CommentDTO;
import com.sky.dto.LikeCommentDTO;
import com.sky.dto.LikeDTO;
import com.sky.entity.Message;
import com.sky.mapper.MessageMapper;
import com.sky.rabbitmq.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl {
    @Autowired
    private MessageMapper messageMapper;
    /**
     * 点赞通知
     */
    @RabbitListener(queues = RabbitMQConfig.MESSAGE_QUEUE_NAME)
    public void receiveLikeMessage(Message message) {
        System.out.println("Received like message: " + message);
        messageMapper.insert(message);
    }

    /**
     * 下单通知
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE_NAME)
    public void receiveOrderMessage(Message message) {
        System.out.println("Received order message: " + message);
        // 这里可以添加处理订单消息的逻辑
    }








}
