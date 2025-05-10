package com.sky.mapper;

import com.sky.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    void insert(Message message);
}
