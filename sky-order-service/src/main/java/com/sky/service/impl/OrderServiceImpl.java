package com.sky.service.impl;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.*;
import com.sky.entity.Order;
import com.sky.entity.SeckillProduct;
import com.sky.entity.UserSeckillRecord;
import com.sky.feign.ProductFeignService;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.utils.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    ProductFeignService productFeignService;

    // 从消息队列取出order然后创建订单，并修改商品库存
    @RabbitListener(queues = RabbitMQConfig.CREATE_ORDER_QUEUE)
    public void createOrder(Order order) {
        //查询秒杀商品单个信息
        Integer seckillId = order.getSeckillId(); // 需根据实际 Order 实体字段调整

        if (seckillId == null) {
            log.error("订单中缺少秒杀ID，无法查询商品信息");
            return;
        }

        // 调用 Feign 接口查询秒杀商品信息，获取价格
        Result<SeckillProduct> result = productFeignService.getSeckillProducts(seckillId);

        // 处理 Feign 调用结果
        if (result != null && result.getCode() == 1) { // 假设 Result 中 code=1 表示成功
            SeckillProduct seckillProduct = result.getData();
            if (seckillProduct == null) {
                log.error("秒杀商品不存在，seckillId: {}", seckillId);
                return;
            }

            // 执行后续逻辑（如校验库存、创建订单等）
            log.info("获取秒杀商品成功：{}", seckillProduct);
            BigDecimal price = seckillProduct.getSeckillPrice();
            Integer account = order.getOrderQuantity();
            BigDecimal total = price.multiply(new BigDecimal(account));
            order.setOrderPrice(total);
            order.setOrderStatus("未支付");
            orderMapper.insertOrder(order);
            //减少普通库存
            productFeignService.updateProducts(seckillProduct.getProductId(),  account);
            //减少限时商品库存
            productFeignService.updateSecProducts(seckillId,  account);

        } else {
            log.error("Feign调用失败，返回码：{}，错误信息：{}",
                    result != null ? result.getCode() : "null",
                    result != null ? result.getMsg() : "无");

        }

    }

    /**
     * 更新订单状态
     * @param orderStatusDTO
     */
    public void updateOrderStatus(OrderStatusDTO orderStatusDTO){
        orderMapper.updateStatus(orderStatusDTO);
    }

    // 定时任务：每 1 分钟检查一次订单状态，10 分钟内未支付的订单设置为已取消
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkOrderStatus() {
        // 计算 10 分钟前的 LocalDateTime
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        orderMapper.findAll().forEach(order -> {
            // 假设 order.getCreateTime() 是 LocalDateTime 类型
            LocalDateTime createTime = order.getCreateTime();
            if (createTime.isBefore(tenMinutesAgo)) { // 使用 isBefore() 比较 LocalDateTime
                OrderStatusDTO orderStatusDTO = OrderStatusDTO.builder()
                        .orderStatus("已取消")
                        .orderId(order.getOrderId())
                        .build();
                orderMapper.updateStatus(orderStatusDTO);
            }
        });
    }

    /**
     * 订单分页
     * @param orderPageDTO
     * @return
     */
    @Override
    public PageResult pageQuery(OrderPageDTO orderPageDTO){
        PageHelper.startPage(orderPageDTO.getPage(),orderPageDTO.getPageSize());
        Page<Order> page = orderMapper.pageQuery(orderPageDTO);
        long total = page.getTotal();
        java.util.List<Order> orders = page.getResult();
        return new PageResult(total,orders);
    }

    /**
     * 秒杀记录详情
     * @param orderSearchDTO
     * @return
     */
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

    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    public Order findOrder(Integer orderId){
        return orderMapper.getOrderById(orderId);
    }
}

