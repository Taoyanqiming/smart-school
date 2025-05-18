package com.sky.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appoint")
@Api(tags = "预约相关接口")
public class AppointController {
    /**
     * 用户创建预约,创建成功后加入消息队列发送到消息模块
     */

    /**
     * 修改预约（修改日期）
     */

    /**
     * 取消预约
     */

    /**
     * 查看预约记录
     */
}
