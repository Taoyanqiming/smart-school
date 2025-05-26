package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.MessageDTO;
import com.sky.dto.SearchDTO;
import com.sky.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {
    /**
     * 收到通知
     * @param messageDTO
     */
    void insert(MessageDTO messageDTO);

    /**
     * 根据条件查询消息列表
     * @param searchDTO 查询条件
     * @return 消息列表
     */
    List<Message> find(SearchDTO searchDTO);

    Page<Message> getMessagePage(Integer type, Integer isRead);
    void updateReadStatus(Integer messageId, Integer isRead);
    void updateReadStatusBatch(List<Integer> messageIds, Integer isRead);
    void updateDeleteFlag(Integer messageId, Integer deleteFlag);
    Integer getUnreadCount();
    Map<Integer, Integer> getCountByType();
}