package com.sky.mapper;

import com.sky.dto.MessageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public class MessageMapper {
    /**
     * 收到点赞通知
     * @param messageDTO
     */
    public void insertLike(MessageDTO messageDTO) {
    }

    public void insertComment(MessageDTO messageDTO) {
    }

    public void insertOrder(MessageDTO messageDTO) {
    }

    public void insertComLike(MessageDTO messageDTO) {
    }
}
