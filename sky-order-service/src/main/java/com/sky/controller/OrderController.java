package com.sky.controller;

import com.sky.dto.*;
import com.sky.entity.Order;
import com.sky.entity.UserSeckillRecord;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * 分页查询订单
     */
    @GetMapping("/list")
    public Result<PageResult> getAllOrders(OrderPageDTO orderPageDTO) {
        PageResult orders = orderService.pageQuery(orderPageDTO);
        return Result.success(orders);
    }

    /**
     * 根据Id查询秒杀记录，用于限购
     */
    @PostMapping("/search")
    public UserSeckillRecord searchOrders(OrderSearchDTO orderSearchDTO) {
        UserSeckillRecord order = orderService.searchOrder(orderSearchDTO);
        return order;
    }
    /**
     * 查看订单详情
     */
    @PostMapping("/order/info")
    public Result<Order> searchOrder(Integer orderId) {
        Order order = orderService.findOrder(orderId);
        return Result.success(order);
    }
    /**
     * 创建秒杀记录
     */
    @PostMapping("/create/seckill")
    public Result createSeck(SeckillCreateDTO seckillCreateDTO){
        orderService.createSeckill(seckillCreateDTO);
        return Result.success();
    }

//    /**
//     * 更新秒杀记录
//     */
//    @PutMapping("/update/seckill")
//    public Result updateSeck(@PathVariable Integer seckillId){
//        orderService.updateSeckill(seckillId);
//        return Result.success();
//    }

    /**
     * 更新订单状态，付款or取消
     */
    @PutMapping("/order/ok")
    public Result updateSeckOK(OrderStatusDTO orderStatusDTO){
        orderService.updateOrderStatus(orderStatusDTO);
        return Result.success();
    }

    /**
     * 删除订单
     */
}
