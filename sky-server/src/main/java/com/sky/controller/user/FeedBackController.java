package com.sky.controller.user;


import com.sky.dto.FeedBacksDTO;
import com.sky.entity.FeedBacks;
import com.sky.result.Result;
import com.sky.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户反馈相关接口")
public class FeedBackController {
    @Autowired
    private MessageService messageService;

    /**
     * 创建反馈信息
     */
    @PostMapping("/create/feedbacks")
    @ApiOperation("用户创建反馈信息")
    public Result createFeedback(@RequestBody FeedBacksDTO feedBacksDTO) {

      messageService.createFeedback(feedBacksDTO);

        return Result.success("反馈信息提交成功");
    }




}
