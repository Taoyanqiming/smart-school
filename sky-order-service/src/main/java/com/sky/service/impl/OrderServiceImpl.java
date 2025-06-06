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
import com.sky.mapper.UserSeckillRecordMapper;
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
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    private UserSeckillRecordMapper userSeckillRecordMapper;


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
        System.out.println("result"+result);

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
            order.setProductId(seckillProduct.getProductId());
            order.setOrderPrice(total);
            order.setCreateTime(LocalDateTime.now());
            order.setOrderStatus("未支付");
            System.out.println("order"+order);
            orderMapper.insertOrder(order);
            System.out.println("插入执行完毕");
            //减少普通库存
            productFeignService.decreaseRepor(seckillId,seckillProduct.getProductId(),  account);
            System.out.println("decreaseRepor执行完毕");
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
        String orderStatus = orderStatusDTO.getOrderStatus();
        if(orderStatus == "已取消"){
            Order order = orderMapper.getOrderById(orderStatusDTO.getOrderId());
            productFeignService.ReturnRepor(order.getSeckillId(),order.getProductId(),order.getOrderQuantity());
        }
        orderMapper.updateStatus(orderStatusDTO);
    }

    // 定时任务：每 1 分钟检查一次订单状态，10 分钟内未支付的订单设置为已取消
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkOrderStatus() {
        // 计算 10 分钟前的 LocalDateTime
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        orderMapper.findAll().forEach(order -> {
            LocalDateTime createTime = order.getCreateTime();
            if (createTime.isBefore(tenMinutesAgo)) {
                //增加库存

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
     * @return
     */
    public UserSeckillRecord searchOrder(Integer userId,Integer seckillId){
        return userSeckillRecordMapper.getRecordByUserIdAndSeckillId(userId,seckillId);

    }

    /**
     * 创建秒杀记录
     * @param seckillCreateDTO
     */
    public void createSeckill(SeckillCreateDTO seckillCreateDTO){
        String RecordId = UUID.randomUUID().toString()+seckillCreateDTO.getSeckillId();
        seckillCreateDTO.setRecordId(RecordId);
        userSeckillRecordMapper.insertRecord(seckillCreateDTO);
    }

    /**
     * 查看订单详情
     * @param orderId
     * @return
     */
    public Order findOrder(String orderId){
        return orderMapper.getOrderById(orderId);
    }
}

