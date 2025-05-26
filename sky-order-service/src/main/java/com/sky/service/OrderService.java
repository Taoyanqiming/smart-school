package com.sky.service;

import com.sky.dto.OrderPageDTO;
import com.sky.dto.OrderSearchDTO;
import com.sky.dto.SeckillCreateDTO;
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
}
