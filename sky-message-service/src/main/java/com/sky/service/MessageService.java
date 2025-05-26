package com.sky.service;


import com.sky.dto.SearchDTO;
import com.sky.entity.Message;
import com.sky.result.PageResult;

import java.util.List;
import java.util.Map;

// com.sky.service.MessageService
public interface MessageService {
    // 原有方法
    List<Message> search(SearchDTO searchDTO);

    // 新增方法
    PageResult getMessagePage(Integer page, Integer pageSize, Integer type, Integer isRead);
    void markAsRead(Integer messageId);
    void markAsReadBatch(List<Integer> messageIds);
    void deleteMessage(Integer messageId);
    Integer getUnreadCount();
    Map<Integer, Integer> getCountByType();
}