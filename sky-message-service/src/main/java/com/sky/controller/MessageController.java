package com.sky.controller;

import com.sky.dto.SearchDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.Message;
import com.sky.result.Result;
import com.sky.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/message")
@Slf4j
@Api(tags = "消息模块")
public class MessageController {
        @Autowired
        private MessageService messageService;


    /**
     * 系统消息
     */


    /**
     * 1.管理员发送系统通知
     */


    /**
     * 2.查询个人系统消息
     */
    @PostMapping("/search")
    @ApiOperation("查询个人消息")
    public List<Message> update(@RequestBody SearchDTO searchDTO) {
        List<Message> messages = messageService.search(searchDTO);
        return messages;
    }

    /**
     * 收到的赞
     */


    /**
     * 回复我的
     */


    /**
     * 订单通知
     */


    /**
     * 预约消息
     */
}
