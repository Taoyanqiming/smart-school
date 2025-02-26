package com.sky.controller.admin;

import com.sky.dto.NoticeDTO;
import com.sky.result.Result;
import com.sky.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "通知相关")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;


    /**
     * 发送通知
     */
    @PostMapping("/create/notice")
    @ApiOperation("发送通知")
    public Result createNotice(@RequestBody NoticeDTO noticeDTO) {
        noticeService.createNotice(noticeDTO);
        return Result.success("发送成功");
    }


    /**
     * 删除通知
     */
    @DeleteMapping("/delete/notice/{noticeId}")
    @ApiOperation("删除通知")
    public Result createNotice(@PathVariable Integer noticeId) {
        noticeService.deleteNotice(noticeId);
        return Result.success("删除成功");
    }
}
