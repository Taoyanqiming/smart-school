package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrderPageDTO;
import com.sky.dto.OrderSearchDTO;
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
//
//    /**
//     * 取消订单
//     * @param params 包含订单 ID 的参数
//     */
//    void cancelOrder(Map<String, Object> params);
//
//    /**
//     * 获取超时未支付的订单
//     * @return 超时未支付的订单列表
//     */
//    List<Order> getExpiredUnpaidOrders();
//
    /**
     * 根据订单 ID 查询订单
     * @param orderId 订单 ID
     * @return 订单信息
     */
    Order getOrderById(Integer orderId);

    /**
     * 订单支付
     * @param orderId 包含订单 ID 和支付状态的参数
     */
    void updatePaymentStatus(Integer orderId);

    /**
     * 查询订单
     * @param orderPageDTO
     * @return
     */
    Page<Order> pageQuery(OrderPageDTO orderPageDTO);

    UserSeckillRecord searchOrder(OrderSearchDTO orderSearchDTO);

    void updateSeckill(SeckillCreateDTO seckillCreateDTO);

    void createSeckill(SeckillCreateDTO seckillCreateDTO);

//    /**
//     * 退款
//     * @param params 包含订单 ID 的参数
//     */
//    void refundOrder(Map<String, Object> params);
}