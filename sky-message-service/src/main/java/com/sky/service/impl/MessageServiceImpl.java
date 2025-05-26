package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.MessageDTO;
import com.sky.dto.SearchDTO;
import com.sky.entity.Message;
import com.sky.mapper.MessageMapper;
import com.sky.result.PageResult;
import com.sky.service.MessageService;
import com.sky.utils.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
        System.out.println("Received like message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insert(messageDTO);
    }

    @RabbitListener(queues = RabbitMQConfig.COMMENT_LIKE_QUEUE)
    public void receiveComLikeMessage(MessageDTO messageDTO) {
        System.out.println("Received 评论点赞 message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insert(messageDTO);
    }
    /**
     * 评论通知
     */
    @RabbitListener(queues = RabbitMQConfig.COMMENT_QUEUE)
    public void receiveCommentMessage(MessageDTO messageDTO) {
        System.out.println("Received Comment message: " + messageDTO);
        //使用openfeign确保对应外键表id存在

        messageMapper.insert(messageDTO);
    }
    /**
     * 订单通知
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrderMessage(MessageDTO messageDTO) {
        System.out.println("Received order message: " + messageDTO);
        //是否使用openfeign确保对应外键表id存在?

        messageMapper.insert(messageDTO);
    }

    /**
     * 预约通知
     */
    @RabbitListener(queues = RabbitMQConfig.APPOINTMENT_QUEUE)
    public void receiveAppointMessage(MessageDTO messageDTO) {
        System.out.println("Received Appoint message: " + messageDTO);
        //是否使用openfeign确保对应外键表id存在?

        messageMapper.insert(messageDTO);
    }
    @Override
    /**
     * 查询消息
     * @param searchDTO
     * @return
     */
    public List<Message> search(SearchDTO searchDTO){
        return messageMapper.find(searchDTO);
    }

    @Override
    public PageResult getMessagePage(Integer page, Integer pageSize, Integer type, Integer isRead) {
        PageHelper.startPage(page, pageSize);
        Page<Message> messagePage = messageMapper.getMessagePage(type, isRead);
        return new PageResult(messagePage.getTotal(), messagePage.getResult());
    }

    @Override
    public void markAsRead(Integer messageId) {
        messageMapper.updateReadStatus(messageId, 1); // 1表示已读
    }

    @Override
    public void markAsReadBatch(List<Integer> messageIds) {
        messageMapper.updateReadStatusBatch(messageIds, 1);
    }

    @Override
    public void deleteMessage(Integer messageId) {
        messageMapper.updateDeleteFlag(messageId, 1); // 1表示已删除
    }

    @Override
    public Integer getUnreadCount() {
        return messageMapper.getUnreadCount();
    }

    @Override
    public Map<Integer, Integer> getCountByType() {
        return messageMapper.getCountByType();
    }
}
