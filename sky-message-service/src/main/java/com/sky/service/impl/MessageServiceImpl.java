package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.GetMessDTO;
import com.sky.entity.MessageDTO;
import com.sky.entity.Message;
import com.sky.mapper.MessageMapper;
import com.sky.result.PageResult;
import com.sky.service.MessageService;
import com.sky.utils.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    /**
     * 点赞通知
     */
    @RabbitListener(queues = RabbitMQConfig.LIKE_QUEUE)
    public void receiveLikeMessage(MessageDTO messageDTO) {
        System.out.println("messageDTO 内容：" + messageDTO);
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO,message);
        System.out.println("message 内容：" + message);
        message.setUserId(messageDTO.getReceiver());
        System.out.println("Received like message: " + messageDTO);
        //使用openfeign确保对应外键表id存在
        messageMapper.insert(message);
    }

    /**
     * 评论点赞
     * @param messageDTO
     */
    @RabbitListener(queues = RabbitMQConfig.COMMENT_LIKE_QUEUE)
    public void receiveComLikeMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO,message);
        message.setUserId(messageDTO.getReceiver());
        System.out.println("Received 评论点赞 message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insert(message);
    }
    /**
     * 评论通知
     */
    @RabbitListener(queues = RabbitMQConfig.COMMENT_QUEUE)
    public void receiveCommentMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO,message);
        message.setUserId(messageDTO.getReceiver());
        System.out.println("Received Comment message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insert(message);
    }
    /**
     * 订单通知
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrderMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO,message);
        System.out.println("Received order message: " + messageDTO);
        //是否使用openfeign确保对应外键表id存在?
        messageMapper.insert(message);
    }

    /**
     * 预约通知
     */
    @RabbitListener(queues = RabbitMQConfig.APPOINTMENT_QUEUE)
    public void receiveAppointMessage(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO,message);
        System.out.println("Received Appoint message: " + messageDTO);
        //是否使用openfeign确保对应外键表id存在?
        messageMapper.insert(message);
    }

    /**
     * 根据条件返回消息
     * @param getMessDTO
     * @return
     */
    @Override
    public PageResult getMessagePage(GetMessDTO getMessDTO) {
        PageHelper.startPage(getMessDTO.getPage(),getMessDTO.getPageSize());
        Page<Message> messagePage = messageMapper.getMessagePage(getMessDTO);
        long total = messagePage.getTotal();
        List<Message> messageList = messagePage.getResult();
        return new PageResult(total,messageList);
    }

    @Override
    public void markAsReadBatch(List<Integer> messageIds) {
        messageMapper.updateReadStatusBatch(messageIds, 1);
    }

    @Override
    public void deleteMessage(Integer messageId) {
        messageMapper.updateDeleteFlag(messageId); // 1表示已删除
    }

    @Override
    public Map<Integer, Integer> getUnreadCount(Integer userId) {
        Map<Integer, Integer> map =  messageMapper.getUnreadCount(userId);
        return map;
    }

    @Override
    public Map<Integer, Integer> getCountByType(Integer userId) {
        return messageMapper.getCountByType(userId);
    }
}
