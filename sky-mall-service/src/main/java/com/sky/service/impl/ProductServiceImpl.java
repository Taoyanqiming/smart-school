package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.feign.OrderFeignService;
import com.sky.mapper.ProductMapper;
import com.sky.mapper.SeckillProductMapper;
import com.sky.rabbitmq.MallSender;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ProductService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    OrderFeignService orderFeignService;
    @Autowired
    private SeckillProductMapper seckillProductMapper;
    @Autowired
    private MallSender mallSender;
    @Autowired
    private DefaultRedisScript<Long> seckillScript;
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 分页获取普通商品
     * @return
     */
    @Override
    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
        Page<Product> page = productMapper.pageQuery(productPageQueryDTO);
        long total = page.getTotal();
        List<Product> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 分页获取秒杀商品
     *
     * @return 包含分页信息和秒杀商品列表的 PageResult 对象
     */
    @Override
    public PageResult getSeckillProducts(SeckillPageQueryDTO seckillPageQueryDTO) {
        // 开启分页查询，设置当前页码和每页显示数量
        PageHelper.startPage(seckillPageQueryDTO.getPage(), seckillPageQueryDTO.getPageSize());
        // 调用 SeckillProductMapper 的 pageQuery 方法进行分页查询，返回一个 Page 对象
        Page<SeckillProduct> page = seckillProductMapper.pageQuery(seckillPageQueryDTO);
        // 获取查询结果的总记录数
        long total = page.getTotal();
        // 获取当前页的秒杀商品记录列表
        List<SeckillProduct> records = page.getResult();
        // 创建一个 PageResult 对象，包含总记录数和当前页的记录列表
        return new PageResult(total, records);
    }

    /**
     * 获取单个秒杀商品
     */
    public SeckillProduct getSeckillProductById(Integer seckillId) {
        if (seckillId == null) {
            return null;
        }
        String seckillKey = "seckill:product:" + seckillId;
        // 1. 优先从 Redis 中获取商品信息
        Map<Object, Object> productMap = redisTemplate.opsForHash().entries(seckillKey);
        if (productMap != null && !productMap.isEmpty()) {
            // 2. 从 Redis 中获取到数据，转换为对象
            return convertMapToSeckillProduct(productMap);
        }
        // 获取锁失败，直接查询数据库（降级处理）
        return seckillProductMapper.getSeckillProductById(seckillId);
    }

    /**
     * 获取单个product详情
     * @param productId
     * @return
     */
    public Product getProductById(Integer productId){
        if (productId == null) {
            return null;
        }
        return productMapper.getProductById(productId);

    }
    /**
     * 将 Map 转换为 SeckillProduct 对象，用于上一个方法
     */
    private SeckillProduct convertMapToSeckillProduct(Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        SeckillProduct product = new SeckillProduct();
        product.setSeckillId(Integer.valueOf(map.get("seckillId").toString()));
        product.setProductId(Integer.valueOf(map.get("productId").toString()));
        product.setSeckillPrice(new BigDecimal(map.get("seckillPrice").toString()));
        product.setSeckillStock(Integer.valueOf(map.get("seckillStock").toString()));
        product.setSeckillLimit(Integer.valueOf(map.get("seckillLimit").toString()));
        return product;
    }

    /**
     * 限时秒杀
     * @param seckillCreateDTO 购买信息
     * @return
     */
    @Override
    public Result<String> purchaseProduct(SeckillCreateDTO seckillCreateDTO) {
        Integer userId = seckillCreateDTO.getUserId();
        Integer seckillId = seckillCreateDTO.getSeckillId();
        Integer quantity = seckillCreateDTO.getPurchaseQuantity();
        String orderId = UUID.randomUUID().toString() + userId;

        // ========== 打印请求参数 ==========
        System.out.println("\n===== 秒杀请求开始 ======");
        System.out.println("请求参数：");
        System.out.println("  userId: " + userId);
        System.out.println("  seckillId: " + seckillId);
        System.out.println("  quantity: " + quantity);
        System.out.println("  orderId: " + orderId);

        if (seckillId == null) {
            System.out.println("错误：秒杀ID为空");
            return Result.error("秒杀ID不能为空");
        }

        // ========== 打印当前系统时间 ==========
        LocalDateTime now = LocalDateTime.now();
        long currentTimeMillis = System.currentTimeMillis();
        long currentTimeSeconds = currentTimeMillis / 1000;
        System.out.println("\n当前系统时间：");
        System.out.println("  本地时间：" + now);
        System.out.println("  毫秒级时间戳：" + currentTimeMillis);
        System.out.println("  秒级时间戳：" + currentTimeSeconds);

        // ========== 读取 Redis 中的秒杀时间 ==========
        String startTimeKey = "seckill:start_time:" + seckillId;
        String endTimeKey = "seckill:end_time:" + seckillId;
        String stockKey = "seckill:stock:" + seckillId;
        String limitKey = "seckill:limit:" + seckillId;

        String startTimeValue = redisTemplate.opsForValue().get(startTimeKey);
        String endTimeValue = redisTemplate.opsForValue().get(endTimeKey);
        String stockValue = redisTemplate.opsForValue().get(stockKey);
        String limitValue = redisTemplate.opsForValue().get(limitKey);

        System.out.println("\nRedis 数据检查：");
        System.out.println("  " + startTimeKey + ": " + startTimeValue);
        System.out.println("  " + endTimeKey + ": " + endTimeValue);
        System.out.println("  " + stockKey + ": " + stockValue);
        System.out.println("  " + limitKey + ": " + limitValue);

        // ========== 执行 Lua 脚本 ==========
        List<String> args = Arrays.asList(
                seckillId.toString(),
                userId.toString(),
                orderId,
                quantity.toString()
        );
        System.out.println("\n执行 Lua 脚本，参数：" + args);

        Long result = redisTemplate.execute(
                seckillScript,
                Collections.emptyList(),
                args.toArray(new String[0])
        );

        System.out.println("\nLua 脚本返回结果：" + result);

        // ========== 处理结果 ==========
        if (result != null && result == 0) {
            System.out.println("秒杀成功，生成订单：" + orderId);
            // 生成订单逻辑...
            Order order=Order.builder()
                    .orderId(orderId)
                    .userId(seckillCreateDTO.getUserId())
                    .seckillId(seckillCreateDTO.getSeckillId())
                    .orderQuantity(seckillCreateDTO.getPurchaseQuantity())
                    .build();
            mallSender.sendOrderMessage(order);
            return Result.success("秒杀成功");
        } else {
            String errorMsg = "未知错误";
            switch (result != null ? result.intValue() : -1) {
                case 1:
                    errorMsg = "秒杀未开始";
                    break;
                case 2:
                    errorMsg = "秒杀已结束";
                    break;
                case 3:
                    errorMsg = "库存不足";
                    break;
                case 4:
                    errorMsg = "超过限购数量";
                    break;
            }
            System.out.println("秒杀失败，原因：" + errorMsg);
            return Result.error(errorMsg);
        }
    }

    /**
     * 普通商品下单
     *
     */
    @Override
    public void purchaseNormalProduct(PurchaseDTO purchaseDTO){
        //根据商品Id查询商品信息
        Product product = productMapper.getProductById(purchaseDTO.getProductId());
        if (product == null) {
            return;
        }
        productMapper.decreaseProductStock(purchaseDTO.getProductId(),purchaseDTO.getQuantity());
        //商品下单
        String orderId = UUID.randomUUID().toString();
        Order order=Order.builder()
                .userId(purchaseDTO.getUserId())
                .orderId(orderId)
                .productId(product.getProductId())
                .build();
        mallSender.sendOrderMessage(order);

    }

    /**
     * 创建普通商品
     * @param productDTO 商品创建信息
     * @return 创建结果，包含商品ID
     */
    @Override
    public Result<Integer> createProduct(ProductCreateDTO productDTO) {
        // 参数校验
        if (productDTO == null) {
            return Result.error("商品信息不能为空");
        }

        if (productDTO.getProductName()==null) {
            return Result.error("商品名称不能为空");
        }

        if (productDTO.getProductPrice() == null || productDTO.getProductPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("商品价格必须大于0");
        }

        if (productDTO.getProductStock() == null || productDTO.getProductStock() < 0) {
            return Result.error("商品库存不能为负数");
        }

        if (productDTO.getProductLimit() == null || productDTO.getProductLimit() <= 0) {
            return Result.error("商品限购数量必须大于0");
        }

        // 构建商品对象
        Product product = Product.builder()
                .productName(productDTO.getProductName())
                .productPrice(productDTO.getProductPrice())
                .productStock(productDTO.getProductStock())
                .productLimit(productDTO.getProductLimit())
                .productImageUrl(productDTO.getProductImageUrl())
                .build();

        // 插入商品到数据库
        productMapper.insert(product);

        // 获取生成的商品ID
        Integer productId = product.getProductId();

        // 返回结果
        return Result.success(productId);
    }


    /**
     * 发布秒杀商品
     * @param seckSetDTO 秒杀商品创建信息
     * @return 操作结果
     */
    @Override
    public Result<String> publishSeckillProduct(SeckSetDTO seckSetDTO) {
        // 参数校验
        if (seckSetDTO == null) {
            return Result.error("参数不能为空");
        }

        // 检查商品是否存在
        Product product = productMapper.getProductById(seckSetDTO.getProductId());
        if (product == null) {
            return Result.error("商品不存在");
        }

        // 构建秒杀商品对象
        SeckillProduct seckillProduct = SeckillProduct.builder()
                .productId(seckSetDTO.getProductId())
                .seckillPrice(seckSetDTO.getSeckillPrice())
                .seckillStartTime(seckSetDTO.getSeckillStartTime())
                .seckillEndTime(seckSetDTO.getSeckillEndTime())
                .seckillStock(seckSetDTO.getSeckillStock())
                .seckillLimit(seckSetDTO.getSeckillLimit())
                .seckillImageUrl(product.getProductImageUrl()) // 使用原商品图片
                .build();

        // 保存秒杀商品到数据库
        seckillProductMapper.insertSeck(seckillProduct);

        // 转换时间为秒级时间戳（UTC 时区）
        LocalDateTime startTime = seckillProduct.getSeckillStartTime();
        LocalDateTime endTime = seckillProduct.getSeckillEndTime();
        long startSeconds = startTime.atZone(ZoneId.of("UTC")).toEpochSecond();
        long endSeconds = endTime.atZone(ZoneId.of("UTC")).toEpochSecond();

        // 构建 Redis Key
        String seckillKey = "seckill:product:" + seckillProduct.getSeckillId();
        String stockKey = "seckill:stock:" + seckillProduct.getSeckillId();
        String startTimeKey = "seckill:start_time:" + seckillProduct.getSeckillId();
        String endTimeKey = "seckill:end_time:" + seckillProduct.getSeckillId();
        String limitKey = "seckill:limit:" + seckillProduct.getSeckillId();



        // 构建秒杀商品信息 Map 时，将所有数值类型转换为字符串
        Map<String, Object> seckillProductMap = new HashMap<>();
        seckillProductMap.put("seckillId", seckillProduct.getSeckillId().toString());
        seckillProductMap.put("productId", seckillProduct.getProductId().toString());
        seckillProductMap.put("seckillPrice", seckillProduct.getSeckillPrice().toString());
        seckillProductMap.put("seckillStartTime", startTime.toString());
        seckillProductMap.put("seckillEndTime", endTime.toString());
        seckillProductMap.put("seckillStock", String.valueOf(seckillProduct.getSeckillStock())); // 使用 String.valueOf()
        seckillProductMap.put("seckillLimit", String.valueOf(seckillProduct.getSeckillLimit())); // 使用 String.valueOf()

        // 存储到 Redis（Hash 类型）
        redisTemplate.opsForHash().putAll(seckillKey, seckillProductMap);

        // 存储独立的时间戳、库存、限购（确保存储为 String）
        redisTemplate.opsForValue().set(stockKey, String.valueOf(seckillProduct.getSeckillStock()));
        redisTemplate.opsForValue().set(startTimeKey, String.valueOf(startSeconds)); // 秒级时间戳
        redisTemplate.opsForValue().set(endTimeKey, String.valueOf(endSeconds));     // 秒级时间戳
        redisTemplate.opsForValue().set(limitKey, String.valueOf(seckillProduct.getSeckillLimit()));


        // 其他逻辑（列表缓存、过期时间）...（同原代码）

        return Result.success("秒杀商品发布成功");
    }

    /**
     * 修改库存,库存减少
     * @param productId
     * @param account
     */
    public void decreaseRepertory(Integer seckillId,Integer productId, Integer account) {
        productMapper.decreaseProductStock(productId, account);
        seckillProductMapper.decreaseSeckillStock(seckillId,account);
    }

    /**
     * 修改限时商品库存退货，库存增加
     * @param seckillId
     * @param account
     */
    public void increaseRepertory(Integer seckillId,Integer productId, Integer account) {
        if (account > 0) {
            // 构建 Redis Key
            String seckillKey = "seckill:product:" + seckillId;
            String stockKey = "seckill:stock:" + seckillId;

            // 分布式锁（按 seckillId 锁定，避免同一秒杀商品的并发问题）
            RLock lock = redissonClient.getLock("lock:seckill_cancel:" + seckillId);

            try {
                // 尝试获取锁，超时时间5秒，自动释放时间10秒
                boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
                if (!isLocked) {
                    throw new RuntimeException("系统繁忙，请稍后重试");
                }
                // 1. 原子性恢复库存（独立库存键）
                redisTemplate.execute((RedisCallback<Long>) connection ->
                        connection.stringCommands().incrBy(
                                stockKey.getBytes(),
                                account.longValue()
                        )
                );
                // 2. 更新 Hash 中的库存字段（可选，保持数据冗余一致性）
                redisTemplate.opsForHash().increment(seckillKey, "seckillStock", account);
                productMapper.increaseProductStock(productId, account);
                seckillProductMapper.increaseSeckillStock(seckillId,account);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("获取分布式锁被中断"+ e);
                throw new RuntimeException("系统内部错误");
            } finally {
                // 释放锁
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }
}
