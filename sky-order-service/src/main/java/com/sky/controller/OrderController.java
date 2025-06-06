package com.sky.controller;

import com.sky.dto.*;
import com.sky.entity.Order;
import com.sky.entity.UserSeckillRecord;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * 分页查询订单
     */
    @PostMapping("/list")
    public Result<PageResult> getAllOrders(@RequestBody OrderPageDTO orderPageDTO) {
        PageResult orders = orderService.pageQuery(orderPageDTO);
        return Result.success(orders);
    }

    /**
     * 根据Id查询秒杀记录，用于限购
     */
    @GetMapping("/search")
    public UserSeckillRecord searchSeckillRecord(@RequestParam Integer seckillId, HttpServletRequest request) {
        Integer userId = Integer.valueOf( request.getHeader("X-User-Id"));
        UserSeckillRecord order = orderService.searchOrder(userId,seckillId);
        return order;
    }
    /**
     * 查看订单详情
     */
    @GetMapping("/order/info")
    public Result<Order> searchOrder(@RequestParam String orderId) {
        Order order = orderService.findOrder(orderId);
        return Result.success(order);
    }
    /**
     * 创建秒杀记录
     */
    @PostMapping("/create/seckill")
    public Result createSeck(@RequestBody SeckillCreateDTO seckillCreateDTO){
        orderService.createSeckill(seckillCreateDTO);
        return Result.success();
    }


    /**
     * 更新订单状态，付款or取消
     */
    @PutMapping("/order/ok")
    public Result updateSeckOK(@RequestBody OrderStatusDTO orderStatusDTO){
        orderService.updateOrderStatus(orderStatusDTO);
        return Result.success();
    }

    /**
     * 删除订单
     */
}
