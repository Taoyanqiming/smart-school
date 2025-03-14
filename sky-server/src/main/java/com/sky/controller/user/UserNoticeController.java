package com.sky.controller.user;

import com.sky.dto.UserPageQueryDTO;
import com.sky.entity.Notice;
import com.sky.exception.UserPageQueryFailedException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户通知相关接口")
public class UserNoticeController {

    @Autowired
    private NoticeService noticeService;
    /**
     * 查看我的通知
     */

    @GetMapping("/notice/{userId}")
    @ApiOperation("通知分页查询")
    public Result<List<Notice>> page(@PathVariable Integer userId) {
        List<Notice> notices = noticeService.selectNoice(userId);
        return Result.success(notices);

    }
}
