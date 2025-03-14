package com.sky.controller.admin;

import com.sky.dto.FeedBacksPageQueryDTO;
import com.sky.dto.RepliesDTO;
import com.sky.entity.Replies;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理员留言反馈管理页面")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 回复反馈信息（管理员使用）
     */
    @PostMapping("/reply")
    @ApiOperation("创建回复")
    public Result reply(RepliesDTO repliesDTO){
        log.info("发送的回复内容为:{}", repliesDTO);
         messageService.reply(repliesDTO);
        return Result.success("回复成功");
    }


    /**
     * 删除反馈信息
     */
    @DeleteMapping("/delete/{feedbackId}")
    @ApiOperation("管理员删除反馈")
    public Result delete(@PathVariable Integer feedbackId){
        messageService.deleteFeedbacks(feedbackId);
        return Result.success("删除成功");
    }

    /**
     * 删除回复信息
     */
    @DeleteMapping("/delete/{replyId}/{feedbackId}")
    @ApiOperation("管理员删除回复")
    public Result deleteReply(@PathVariable Integer replyId,@PathVariable Integer feedbackId){
        messageService.deleteReplies(replyId,feedbackId);
        return Result.success("删除成功");
    }


}
