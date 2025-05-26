package com.sky.controller;

import com.sky.dto.OrderPageDTO;
import com.sky.dto.OrderSearchDTO;
import com.sky.dto.SeckillCreateDTO;
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
     * 查询订单
     */
    @GetMapping("/list")
    public Result<PageResult> getAllOrders(OrderPageDTO orderPageDTO) {
        PageResult orders = orderService.pageQuery(orderPageDTO);
        return Result.success(orders);
    }

    /**
     * 根据用户id和商品Id查询订单
     */
    @GetMapping("/search")
    public UserSeckillRecord searchOrders(OrderSearchDTO orderSearchDTO) {
        UserSeckillRecord order = orderService.searchOrder(orderSearchDTO);
        return order;
    }

    /**
     * 创建秒杀记录
     */
    @PostMapping("/create/seckill")
    public Result createSeck(SeckillCreateDTO seckillCreateDTO){
        orderService.createSeckill(seckillCreateDTO);
        return Result.success();
    }

    /**
     * 更新秒杀记录
     */
    @PutMapping("/update/seckill")
    public Result updateSeck(SeckillCreateDTO seckillCreateDTO){
        orderService.updateSeckill(seckillCreateDTO);
        return Result.success();
    }
}
