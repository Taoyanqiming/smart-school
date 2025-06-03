package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrderPageDTO;
import com.sky.dto.OrderSearchDTO;
import com.sky.dto.OrderStatusDTO;
import com.sky.dto.SeckillCreateDTO;
import com.sky.entity.Order;
import com.sky.entity.UserSeckillRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
import java.util.List;
/**
 * 订单数据访问层接口
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入订单信息
     * @param order 订单信息
     */
    void insertOrder(Order order);

    /**
     * 根据订单 ID 查询订单
     * @param orderId 订单 ID
     * @return 订单信息
     */
    Order getOrderById(Integer orderId);


    /**
     * 查询订单
     * @param orderPageDTO
     * @return
     */
    Page<Order> pageQuery(OrderPageDTO orderPageDTO);

    UserSeckillRecord searchOrder(OrderSearchDTO orderSearchDTO);

    void updateSeckill(SeckillCreateDTO seckillCreateDTO);

    void createSeckill(SeckillCreateDTO seckillCreateDTO);

    /**
     * 更新订单状态
     * @param orderStatusDTO
     */
    void updateStatus(OrderStatusDTO orderStatusDTO);

    /**
     * 查询所有未支付订单
     * @return
     */
    List<Order> findAll();
}