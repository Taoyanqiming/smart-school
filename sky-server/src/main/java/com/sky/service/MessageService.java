package com.sky.service;

import com.sky.dto.CommentDTO;
import com.sky.entity.Message;

public  interface MessageService {
    /**
     * 处理点赞消息
     * @param message 点赞消息实体
     */
    void receiveLikeMessage(Message message);



    /**
     * 处理订单消息
     * @param message 订单消息实体
     */
    void receiveOrderMessage(Message message);


}
