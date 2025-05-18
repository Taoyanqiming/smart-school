package com.sky.service;


import com.sky.dto.SearchDTO;
import com.sky.entity.Message;

import java.util.List;

public interface MessageService {
    /**
     * 查询消息
     * @param searchDTO
     * @return
     */
     List<Message> search(SearchDTO searchDTO);
}