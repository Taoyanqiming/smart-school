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
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, Object> redisTemplate;
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
    public PageResult getSeckillProducts(ProductPageQueryDTO productPageQueryDTO) {
        // 开启分页查询，设置当前页码和每页显示数量
        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
        // 调用 SeckillProductMapper 的 pageQuery 方法进行分页查询，返回一个 Page 对象
        Page<SeckillProduct> page = seckillProductMapper.pageQuery(productPageQueryDTO);
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
        String orderId = UUID.randomUUID().toString();
        System.out.println("UUID.randomUUID()"+orderId);
        if (seckillId == null) {
            return Result.error("秒杀ID不能为空");
        }

        List<String> args = Arrays.asList(
                seckillId.toString(),
                userId.toString(),
                orderId,
                quantity.toString()
        );

        Long result = redisTemplate.execute(
                seckillScript,
                Collections.emptyList(),
                args.toArray(new String[0])
        );

        if (result != null && result == 0) {
            // 秒杀成功逻辑...

            Order order=Order.builder()
                    .orderId(orderId)
                    .userId(seckillCreateDTO.getUserId())
                    .productId(seckillCreateDTO.getSeckillId())
                    .orderQuantity(seckillCreateDTO.getPurchaseQuantity())
                    .orderTime(LocalDateTime.now())
                    .build();
            mallSender.sendOrderMessage(order);

            return Result.success("秒杀成功");

        } else {
            // 处理失败情况，构建统一结果
            Result<String> resultObj;
            switch (result.intValue()) {
                case 1:
                    resultObj = Result.error("秒杀未开始");
                    break;
                case 2:
                    resultObj = Result.error("秒杀已结束");
                    break;
                case 3:
                    resultObj = Result.error("库存不足");
                    break;
                case 4:
                    resultObj = Result.error("超过限购数量");
                    break;
                default:
                    resultObj = Result.error("秒杀失败，请重试");
                    break;
            }
            return resultObj;
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
        String seckillKey = "seckill:product:" + seckillProduct.getSeckillId(); //限购商品全部信息
        String stockKey = "seckill:stock:" + seckillProduct.getSeckillId();  //库存
        String startTimeKey = "seckill:start_time:" + seckillProduct.getSeckillId(); // 开始时间
        String endTimeKey = "seckill:end_time:" + seckillProduct.getSeckillId();   // 结束时间
        String limitKey = "seckill:limit:" + seckillProduct.getSeckillId();       // 新增限购 Key

        // 存储秒杀商品信息（Hash）
        Map<String, Object> seckillProductMap = new HashMap<>();
        seckillProductMap.put("seckillId", seckillProduct.getSeckillId());
        seckillProductMap.put("productId", seckillProduct.getProductId());
        seckillProductMap.put("seckillPrice", seckillProduct.getSeckillPrice().toString());
        // 时间存储为字符串（原代码保留，供业务查询使用，但 Lua 脚本不依赖这里的时间）
        seckillProductMap.put("seckillStartTime", startTime.toString());
        seckillProductMap.put("seckillEndTime", endTime.toString());
        seckillProductMap.put("seckillStock", seckillProduct.getSeckillStock());
        seckillProductMap.put("seckillLimit", seckillProduct.getSeckillLimit());
        redisTemplate.opsForHash().putAll(seckillKey, seckillProductMap);

        // 存储独立的时间戳、库存、限购
        redisTemplate.opsForValue().set(stockKey, seckillProduct.getSeckillStock().toString());
        redisTemplate.opsForValue().set(startTimeKey, String.valueOf(startSeconds)); // 秒级时间戳
        redisTemplate.opsForValue().set(endTimeKey, String.valueOf(endSeconds));     // 秒级时间戳
        redisTemplate.opsForValue().set(limitKey, seckillProduct.getSeckillLimit().toString());

        // 其他逻辑（列表缓存、过期时间）...（同原代码）

        return Result.success("秒杀商品发布成功");
    }


    /**
     * 修改库存
     * @param productId
     * @param account
     */
    public void updateRepertory(Integer productId, Integer account) {
        productMapper.decreaseProductStock(productId, account);
    }
    /**
     * 修改限时商品库存
     * @param seckillId
     * @param account
     */
    public void updateSecRepertory(Integer seckillId, Integer account) {
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
