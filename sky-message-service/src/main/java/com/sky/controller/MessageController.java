package com.sky.controller;

import com.sky.dto.SearchDTO;
import com.sky.dto.UserDTO;
import com.sky.entity.Message;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// com.sky.controller.MessageController
@RestController
@RequestMapping("/user/message")
@Slf4j
@Api(tags = "消息模块")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 分页查询个人消息
     */
    @GetMapping("/page")
    @ApiOperation("分页查询个人消息")
    public Result<PageResult> getMessagePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer isRead
    ) {
        PageResult pageResult = messageService.getMessagePage(page, pageSize, type, isRead);
        return Result.success(pageResult);
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{messageId}")
    @ApiOperation("标记消息为已读")
    public Result<String> markAsRead(@PathVariable Integer messageId) {
        messageService.markAsRead(messageId);
        return Result.success("标记成功");
    }

    /**
     * 批量标记消息为已读
     */
    @PutMapping("/read/batch")
    @ApiOperation("批量标记消息为已读")
    public Result<String> markAsReadBatch(@RequestBody List<Integer> messageIds) {
        messageService.markAsReadBatch(messageIds);
        return Result.success("批量标记成功");
    }

    /**
     * 删除消息
     */
    @DeleteMapping("/{messageId}")
    @ApiOperation("删除消息")
    public Result<String> deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
        return Result.success("删除成功");
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    @ApiOperation("获取未读消息数量")
    public Result<Integer> getUnreadCount() {
        Integer count = messageService.getUnreadCount();
        return Result.success(count);
    }

    /**
     * 按类型分组统计消息数量
     */
    @GetMapping("/count/by-type")
    @ApiOperation("按类型分组统计消息数量")
    public Result<Map<Integer, Integer>> getCountByType() {
        Map<Integer, Integer> countMap = messageService.getCountByType();
        return Result.success(countMap);
    }
}