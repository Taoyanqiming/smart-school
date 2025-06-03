package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.GetMessDTO;
import com.sky.entity.Message;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {
    /**
     * 收到通知
     * @param message
     */
    @AutoFill(OperationType.INSERT_CREATE_ONLY)
    void insert(Message message);

    /**
     * 根据条件查询消息列表
     * @param getMessDTO 查询条件
     * @return 消息列表
     */
    Page<Message> getMessagePage(GetMessDTO getMessDTO);

    void updateReadStatusBatch(@Param("list") List<Integer> messageIds,
                               @Param("isRead") Integer isRead);
    void updateDeleteFlag(Integer messageId);
    Map<Integer, Integer> getUnreadCount(@Param("userId") Integer userId);

    Map<Integer, Integer> getCountByType(@Param("userId") Integer userId);
}