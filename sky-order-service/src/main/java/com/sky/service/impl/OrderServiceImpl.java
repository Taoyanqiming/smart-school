package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.OrderPageDTO;
import com.sky.dto.OrderSearchDTO;
import com.sky.dto.SeckillCreateDTO;
import com.sky.entity.Order;
import com.sky.entity.UserSeckillRecord;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {
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
    @Override
    public PageResult pageQuery(OrderPageDTO orderPageDTO){
        PageHelper.startPage(orderPageDTO.getPage(),orderPageDTO.getPageSize());
        Page<Order> page = orderMapper.pageQuery(orderPageDTO);
        long total = page.getTotal();
        java.util.List<Order> orders = page.getResult();
        return new PageResult(total,orders);
    }

    public UserSeckillRecord searchOrder(OrderSearchDTO orderSearchDTO){
        return orderMapper.searchOrder(orderSearchDTO);

    }

    /**
     * 创建秒杀记录
     * @param seckillCreateDTO
     */
    public void createSeckill(SeckillCreateDTO seckillCreateDTO){
        orderMapper.createSeckill(seckillCreateDTO);
    }
    /**
     * 更新秒杀记录
     * @param seckillCreateDTO
     */
    public void updateSeckill(SeckillCreateDTO seckillCreateDTO){
        Integer userId = seckillCreateDTO.getUserId();
        Integer seckillId = seckillCreateDTO.getSeckillId();
        Integer purchaseQuantity = seckillCreateDTO.getPurchaseQuantity();
        //先查询

        //if存在，则修改
        orderMapper.updateSeckill(seckillCreateDTO);
    }
}

//
//    /**
//     * 从 Redis 消息队列中消费订单请求
//     */
//    @Transactional
//    public void consumeOrderQueue() {
//        // 从 Redis 消息队列中取出一个订单请求
//        PurchaseDTO purchaseDTO = (PurchaseDTO) redisTemplate.opsForList().leftPop(ORDER_QUEUE_KEY);
//        if (purchaseDTO != null) {
//            // 若订单请求不为空，从 PurchaseDTO 中获取用户 ID
//            Integer userId = purchaseDTO.getUserId();
//            // 从 PurchaseDTO 中获取商品 ID
//            Integer productId = purchaseDTO.getProductId();
//            // 从 PurchaseDTO 中获取秒杀商品 ID
//            Integer seckillId = purchaseDTO.getSeckillId();
//            // 从 PurchaseDTO 中获取购买数量
//            Integer quantity = purchaseDTO.getQuantity();
//
//            // 生成订单对象
//            Order order = new Order();
//            // 设置订单的用户 ID
//            order.setUserId(userId);
//            // 设置订单的商品 ID
//            order.setProductId(productId);
//            // 设置订单的秒杀商品 ID
//            order.setSeckillId(seckillId);
//            // 设置订单的购买数量
//            order.setOrderQuantity(quantity);
//            if (seckillId != null) {
//                // 若秒杀商品 ID 不为空，根据秒杀商品 ID 查询秒杀商品信息
//                SeckillProduct seckillProduct = seckillProductMapper.getSeckillProductById(seckillId);
//                // 计算订单的总价
//                order.setOrderPrice(seckillProduct.getSeckillPrice().multiply(BigDecimal.valueOf(quantity)));
//            } else {
//                // 若秒杀商品 ID 为空，根据商品 ID 查询普通商品信息
//                Product product = productMapper.getProductById(productId);
//                // 计算订单的总价
//                order.setOrderPrice(product.getProductPrice().multiply(BigDecimal.valueOf(quantity)));
//            }
//            // 设置订单状态为未支付
//            order.setOrderStatus("未支付");
//            // 设置订单的下单时间为当前时间
//            order.setOrderTime(new Date());
//            // 计算付款截止时间，当前时间 + 10 分钟
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.MINUTE, 10);
//            order.setPaymentDeadline(calendar.getTime());
//            order.setPaymentStatus("未支付");
//            // 将订单信息插入数据库
//            orderMapper.insertOrder(order);
//
//            // 使用 Redis 缓存订单信息，设置缓存过期时间为 10 分钟
//            redisTemplate.opsForValue().set("order:" + order.getOrderId(), order, 10, TimeUnit.MINUTES);
//        }
//    }

//    /**
//     * 取消超时未支付的订单
//     */
//    @Scheduled(fixedRate = 60000) // 每分钟执行一次
//    public void cancelExpiredOrders() {
//        // 查询所有未支付的订单
//        // 这里可以根据实际情况实现查询逻辑，假设 OrderMapper 有相应方法
//        List<Order> unpaidOrders = new ArrayList<>(); // 实际实现中需要查询数据库获取未支付订单
//
//        for (Order order : unpaidOrders) {
//            if (order.getPaymentDeadline() != null && new Date().after(order.getPaymentDeadline())) {
//                // 订单已超时，取消订单
//                Map<String, Object> params = new HashMap<>();
//                params.put("orderId", order.getOrderId());
//                orderMapper.cancelOrder(params);
//
//                // 恢复库存
//                if (order.getSeckillId() != null) {
//                    seckillProductMapper.increaseSeckillStock(order.getSeckillId(), order.getOrderQuantity());
//                    UserSeckillRecord record = userSeckillRecordMapper.getRecordByUserIdAndSeckillId(order.getUserId(), order.getSeckillId());
//                    if (record != null) {
//                        userSeckillRecordMapper.updateRecord(order.getUserId(), order.getSeckillId(), record.getPurchaseQuantity() - order.getOrderQuantity());
//                    }
//                } else {
//                    productMapper.increaseProductStock(order.getProductId(), order.getOrderQuantity());
//                }
//            }
//        }
//    }
//
//    /**
//     * 模拟支付功能
//     */
//    public boolean simulatePayment(Integer orderId) {
//        Order order = orderMapper.getOrderById(orderId);
//        if (order != null && "未支付".equals(order.getPaymentStatus())) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("orderId", orderId);
//            params.put("paymentStatus", "已支付");
//            order.setOrderStatus("已支付");
//            order.setPaymentTime(new Date());
//            orderMapper.updatePaymentStatus(params);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 退款功能
//     */
//    public boolean refundOrder(Integer orderId) {
//        Order order = orderMapper.getOrderById(orderId);
//        if (order != null && "已支付".equals(order.getPaymentStatus())) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("orderId", orderId);
//            params.put("paymentStatus", "已退款");
//            params.put("orderStatus", "已退款");
//            orderMapper.refundOrder(params);
//
//            // 恢复库存
//            if (order.getSeckillId() != null) {
//                seckillProductMapper.increaseSeckillStock(order.getSeckillId(), order.getOrderQuantity());
//                UserSeckillRecord record = userSeckillRecordMapper.getRecordByUserIdAndSeckillId(order.getUserId(), order.getSeckillId());
//                if (record != null) {
//                    userSeckillRecordMapper.updateRecord(order.getUserId(), order.getSeckillId(), record.getPurchaseQuantity() - order.getOrderQuantity());
//                }
//            } else {
//                productMapper.increaseProductStock(order.getProductId(), order.getOrderQuantity());
//            }
//            return true;
//        }
//        return false;
//    }
