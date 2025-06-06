package com.sky.controller;

import com.sky.dto.GetMessDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 分页查询消息
     */
    @PostMapping("/page")
    public Result<PageResult> getMessagePage(@RequestBody GetMessDTO getMessDTO) {
        PageResult pageResult = messageService.getMessagePage(getMessDTO);
        return Result.success(pageResult);
    }


    /**
     * 批量标记消息为已读
     */
    @PutMapping("/read/batch")
    public Result<String> markAsReadBatch(@RequestBody List<Integer> messageIds) {
        messageService.markAsReadBatch(messageIds);
        return Result.success("批量标记成功");
    }

    /**
     * 删除消息
     */
    @DeleteMapping("/{messageId}")
    public Result<String> deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
        return Result.success("删除成功");
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread/count")
    public Result<Map<Integer, Integer>> getUnreadCount(HttpServletRequest request) {
        Integer userId = Integer.valueOf( request.getHeader("X-User-Id"));
        Map<Integer, Integer> count = messageService.getUnreadCount(userId);
        return Result.success(count);
    }

    /**
     * 按类型分组统计消息数量
     */
    @GetMapping("/count/by-type")
    public Result<Map<Integer, Integer>> getCountByType(HttpServletRequest request) {
        Integer userId = Integer.valueOf( request.getHeader("X-User-Id"));
        Map<Integer, Integer> countMap = messageService.getCountByType(userId);
        return Result.success(countMap);
    }
}