package com.sky.service.impl;
import com.sky.entity.Order;
import com.sky.mapper.OrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderServiceImpl {
    @Autowired
    private OrderMapper orderMapper;

    // 从消息队列取出order然后创建订单
    @RabbitListener(queues = "order_queue") // 假设消息队列名为 order_queue
    public void createOrder(Order order) {

        orderMapper.insertOrder(order);
    }

    // 订单支付方法
    public void payOrder(Integer orderId) {
        Order order = orderMapper.getOrderById(orderId);
        if (order != null) {

            orderMapper.updatePaymentStatus(orderId);
        }
    }

//    // 定时任务：每 1 分钟检查一次订单状态，10 分钟内未支付的订单设置为已取消
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void checkOrderStatus() {
//        Date tenMinutesAgo = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
//        orderRepository.findAll().forEach(order -> {
//            if (Order.OrderStatus.未支付.equals(order.getOrderStatus()) && order.getOrderTime().before(tenMinutesAgo)) {
//                order.setOrderStatus(Order.OrderStatus.已取消);
//                orderRepository.save(order);
//            }
//        });
//    }
}
