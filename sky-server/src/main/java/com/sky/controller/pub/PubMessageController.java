package com.sky.controller.pub;

import com.sky.dto.FeedBacksPageQueryDTO;
import com.sky.dto.RepliesDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Slf4j
@Api(tags = "未登录可查看留言反馈页面")
public class PubMessageController {
    @Autowired
    private MessageService messageService;
    /**
     * 分页查看所有反馈信息
     */
    @GetMapping("/page/message")
    @ApiOperation("查询反馈信息")
    public Result<PageResult> getMessageList(FeedBacksPageQueryDTO feedBacksPageQueryDTO){
        log.info("feedBacksPageQueryDTO:{}", feedBacksPageQueryDTO);
        PageResult pageResult = messageService.getMessageList(feedBacksPageQueryDTO);
        return  Result.success(pageResult);
    }

}
