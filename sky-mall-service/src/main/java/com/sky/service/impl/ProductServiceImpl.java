package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.sky.context.BaseContext;
import com.sky.dto.PurchaseDTO;
import com.sky.entity.*;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ProductMapper;

import com.sky.mapper.SeckillProductMapper;
import com.sky.mapper.UserSeckillRecordMapper;
import com.sky.rabbitmq.MessageSender;
import com.sky.result.PageResult;
import com.sky.service.ProductService;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import java.util.concurrent.TimeUnit;

/**
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SeckillProductMapper seckillProductMapper;
    @Autowired
    private UserSeckillRecordMapper userSeckillRecordMapper;
    @Autowired
    private MessageSender messageSender;
    private static final String LUA_SCRIPT = "local seckillId = ARGV[1]\n" +
            "-- 用户 ID\n" +
            "local userId = ARGV[2]\n" +
            "-- 订单 ID\n" +
            "local orderId = ARGV[3]\n" +
            "-- 购买数量\n" +
            "local quantity = tonumber(ARGV[4])\n" +
            "\n" +
            "-- 库存 key\n" +
            "local stockKey = 'seckill:stock:' .. seckillId\n" +
            "-- 订单 key\n" +
            "local orderKey = 'seckill:order:' .. seckillId\n" +
            "-- 用户购买记录 key\n" +
            "local userRecordKey = 'seckill:user_record:' .. seckillId .. ':' .. userId\n" +
            "\n" +
            "-- 获取当前时间（假设 Redis 服务器时间和业务系统时间一致）\n" +
            "local currentTime = tonumber(redis.call('time')[1])\n" +
            "\n" +
            "-- 从 Redis 中获取秒杀开始时间和结束时间（需要提前将这些信息存入 Redis）\n" +
            "local startTimeKey = 'seckill:start_time:' .. seckillId\n" +
            "local endTimeKey = 'seckill:end_time:' .. seckillId\n" +
            "local startTime = tonumber(redis.call('get', startTimeKey))\n" +
            "local endTime = tonumber(redis.call('get', endTimeKey))\n" +
            "\n" +
            "-- 判断秒杀活动是否开始\n" +
            "if startTime == nil or currentTime < startTime then\n" +
            "    return 1 -- 秒杀未开始\n" +
            "end\n" +
            "\n" +
            "-- 判断秒杀活动是否结束\n" +
            "if endTime == nil or currentTime > endTime then\n" +
            "    return 2 -- 秒杀已结束\n" +
            "end\n" +
            "\n" +
            "-- 库存是否充足\n" +
            "local stock = tonumber(redis.call('get', stockKey))\n" +
            "if stock == nil or stock < quantity then\n" +
            "    return 3 -- 库存不足\n" +
            "end\n" +
            "\n" +
            "-- 获取用户已购买数量\n" +
            "local userPurchased = tonumber(redis.call('get', userRecordKey)) or 0\n" +
            "\n" +
            "-- 从 Redis 中获取该秒杀商品的限购数量（需要提前将这些信息存入 Redis）\n" +
            "local limitKey = 'seckill:limit:' .. seckillId\n" +
            "local limit = tonumber(redis.call('get', limitKey))\n" +
            "\n" +
            "-- 判断用户是否超过限购数量\n" +
            "if limit ~= nil and userPurchased + quantity > limit then\n" +
            "    return 4 -- 超过限购数量\n" +
            "end\n" +
            "\n" +
            "-- 扣减库存\n" +
            "redis.call('incrby', stockKey, -quantity)\n" +
            "\n" +
            "-- 更新用户购买记录\n" +
            "redis.call('incrby', userRecordKey, quantity)\n" +
            "\n" +
            "-- 下单（保存用户）\n" +
            "redis.call('sadd', orderKey, userId)\n" +
            "\n" +
            "-- 发送消息\n" +
            "redis.call('xadd', 'stream.orders', '*', 'userId', userId, 'seckillId', seckillId, 'orderId', orderId, 'quantity', quantity)\n" +
            "\n" +
            "return 0 -- 秒杀成功";
//    /**
//     * 获取普通商品
//     * @return
//     */
//    @Override
//    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
//        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
//        Page<Product> page = productMapper.pageQuery(productPageQueryDTO);
//        long total = page.getTotal();
//        java.util.List<Product> records = page.getResult();
//        return new PageResult(total, records);
//    }

//    /**
//     * 获取秒杀商品
//     * @return 包含分页信息和秒杀商品列表的 PageResult 对象
//     */
//    @Override
//    public PageResult getSeckillProducts(ProductPageQueryDTO productPageQueryDTO) {
//        // 开启分页查询，设置当前页码和每页显示数量
//        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
//        // 调用 SeckillProductMapper 的 pageQuery 方法进行分页查询，返回一个 Page 对象
//        Page<SeckillProduct> page = seckillProductMapper.pageQuery(productPageQueryDTO);
//        // 获取查询结果的总记录数
//        long total = page.getTotal();
//        // 获取当前页的秒杀商品记录列表
//        java.util.List<SeckillProduct> records = page.getResult();
//        // 创建一个 PageResult 对象，包含总记录数和当前页的记录列表
//        return new PageResult(total, records);
//    }
        /**
         * 普通商品下单
         *
         */
        @Override
        public void purchase(PurchaseDTO purchaseDTO){
            //根据商品Id查询商品信息
            Product product=productMapper.getProductById(purchaseDTO.getProductId());
            //商品下单
            int userId = BaseContext.getCurrentId();
            Order order=Order.builder()
                    .userId(userId)
                    .productId(product.getProductId())
                    .paymentStatus("未支付")
                    .build();
            messageSender.sendOrderMessage(order);
            Message message = Message.builder()
                    .userId(userId)
                    .messageContent("你有新的订单消息")
                    .messageType("place_order")
                    .build();
            //创建message加入消息队列
            messageSender.sendMessage(message);
        }

    @SuppressWarnings("unchecked")
    @Override
    public boolean purchaseProduct(PurchaseDTO purchaseDTO) {
        // 从 PurchaseDTO 中获取用户 ID
        Integer userId = purchaseDTO.getUserId();
        // 从 PurchaseDTO 中获取秒杀商品 ID
        Integer seckillId = purchaseDTO.getSeckillId();
        // 从 PurchaseDTO 中获取购买数量
        Integer quantity = purchaseDTO.getQuantity();

        // 若秒杀商品 ID 不为空，进行秒杀商品处理
        if (seckillId != null) {
            // 根据秒杀商品 ID 查询秒杀商品信息
            SeckillProduct seckillProduct = seckillProductMapper.getSeckillProductById(seckillId);
            // 若秒杀商品不存在或者库存不足，返回购买失败
            if (seckillProduct == null || seckillProduct.getSeckillStock() < quantity) {
                return false;
            }
            // 根据用户 ID 和秒杀商品 ID 查询用户的秒杀记录
            UserSeckillRecord record = userSeckillRecordMapper.getRecordByUserIdAndSeckillId(userId, seckillId);
            // 若用户已有秒杀记录且本次购买数量加上已购买数量超过秒杀限购数量，返回购买失败
            if (record != null && record.getPurchaseQuantity() + quantity > seckillProduct.getSeckillLimit()) {
                return false;
            }
            // 使用 Redis 分布式锁保证高并发下库存扣减的原子性，生成锁的键
            String lockKey = "seckill_lock:" + seckillId;
            // 尝试获取分布式锁，设置锁的过期时间为 10 秒
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);
            if (lock != null && lock) {
                try {
                    // 生成订单 ID（这里简单模拟，实际中可能需要更复杂的生成逻辑）
                    String orderId = String.valueOf(System.currentTimeMillis());
                    // 执行 Lua 脚本
                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
                    List<String> keys = Arrays.asList();
                    List<String> args = Arrays.asList(seckillId.toString(), userId.toString(), orderId, quantity.toString());
                    Long result = redisTemplate.execute(redisScript, keys, args.toArray());
                    if (result == 0) {
                        if (record == null) {
                            // 若用户没有秒杀记录，插入新的秒杀记录
                            userSeckillRecordMapper.insertRecord(userId, seckillId, quantity);
                        } else {
                            // 若用户已有秒杀记录，更新秒杀记录中的购买数量
                            userSeckillRecordMapper.updateRecord(userId, seckillId, record.getPurchaseQuantity() + quantity);
                        }
                    } else {
                        return false;
                    }
                } finally {
                    // 释放分布式锁
                    redisTemplate.delete(lockKey);
                }
            } else {
                // 若未获取到分布式锁，返回购买失败
                return false;
            }
        } else {
            // 若秒杀商品 ID 为空，直接返回失败，因为该方法只处理秒杀商品
            return false;
        }

        // 对于秒杀商品下单，是否有必要加入消息队列分开进行，取决于具体业务需求
        // 如果秒杀商品下单后，需要异步处理一些任务，如通知用户、记录日志等，加入消息队列是有必要的
        // 如果这些任务可以在下单成功后直接同步处理，也可以不加入消息队列
        // 这里假设需要加入消息队列
        Order order = Order.builder()
                .userId(userId)
                .productId(seckillId) // 这里假设秒杀商品 ID 可作为商品 ID 处理，实际可能需要调整
                .paymentStatus("未支付")
                .build();
        messageSender.sendOrderMessage(order);
        return true;
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
}