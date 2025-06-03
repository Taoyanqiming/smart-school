package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Order;
import com.sky.entity.UserSeckillRecord;
import com.sky.result.PageResult;

public interface OrderService {
    PageResult pageQuery(OrderPageDTO orderPageDTO);

    /**
     * 查询订单
     * @param orderSearchDTO
     * @return
     */
    UserSeckillRecord searchOrder(OrderSearchDTO orderSearchDTO);

    /**
     * 创建秒杀记录
     * @param seckillCreateDTO
     */
    void createSeckill(SeckillCreateDTO seckillCreateDTO);
    /**
     * 更新秒杀记录
     * @param seckillCreateDTO
     */
    void updateSeckill(SeckillCreateDTO seckillCreateDTO);

    /**
     * 更新订单状态
     * @param orderStatusDTO
     */
    void updateOrderStatus(OrderStatusDTO orderStatusDTO);

    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    Order findOrder(Integer orderId);
}
