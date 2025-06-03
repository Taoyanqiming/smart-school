package com.sky.service;


import com.sky.dto.GetMessDTO;
import com.sky.result.PageResult;

import java.util.List;
import java.util.Map;

// com.sky.service.MessageService
public interface MessageService {

    // 新增方法
    PageResult getMessagePage(GetMessDTO getMessDTO);

    void markAsReadBatch(List<Integer> messageIds);
    void deleteMessage(Integer messageId);

    Map<Integer, Integer> getUnreadCount(Integer userId);
    Map<Integer, Integer> getCountByType(Integer userId);
}