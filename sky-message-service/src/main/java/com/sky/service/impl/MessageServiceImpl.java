package com.sky.service.impl;

import com.sky.dto.MessageDTO;
import com.sky.dto.SearchDTO;
import com.sky.entity.Message;
import com.sky.mapper.MessageMapper;
import com.sky.service.MessageService;
import com.sky.utils.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    /**
     * 点赞通知
     */
    @RabbitListener(queues = RabbitMQConfig.LIKE_QUEUE)
    public void receiveLikeMessage(MessageDTO messageDTO) {
        System.out.println("Received like message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insertLike(messageDTO);
    }

    @RabbitListener(queues = RabbitMQConfig.COMMENT_LIKE_QUEUE)
    public void receiveComLikeMessage(MessageDTO messageDTO) {
        System.out.println("Received 评论点赞 message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insertComLike(messageDTO);
    }
    /**
     * 评论通知
     */
    @RabbitListener(queues = RabbitMQConfig.COMMENT_QUEUE)
    public void receiveCommentMessage(MessageDTO messageDTO) {
        System.out.println("Received Comment message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insertComment(messageDTO);
    }
    /**
     * 订单通知
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrderMessage(MessageDTO messageDTO) {
        System.out.println("Received order message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insertOrder(messageDTO);
    }
    /**
     * 预约通知
     */

    @Override
    /**
     * 查询消息
     * @param searchDTO
     * @return
     */
    public List<Message> search(SearchDTO searchDTO){

    }
}
