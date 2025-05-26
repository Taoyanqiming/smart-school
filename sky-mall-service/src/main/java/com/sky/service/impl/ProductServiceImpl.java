package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.OrderSearchDTO;
import com.sky.dto.ProductPageQueryDTO;
import com.sky.dto.PurchaseDTO;
import com.sky.dto.SeckillCreateDTO;
import com.sky.entity.*;
import com.sky.feign.OrderFeignService;
import com.sky.mapper.ProductMapper;
import com.sky.mapper.SeckillProductMapper;
import com.sky.rabbitmq.MallSender;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ProductService;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

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
    /**
     * 获取普通商品
     *
     * @return
     */
    @Override
    public PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO) {
        PageHelper.startPage(productPageQueryDTO.getPage(), productPageQueryDTO.getPageSize());
        Page<Product> page = productMapper.pageQuery(productPageQueryDTO);
        long total = page.getTotal();
        java.util.List<Product> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 获取秒杀商品
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
        java.util.List<SeckillProduct> records = page.getResult();
        // 创建一个 PageResult 对象，包含总记录数和当前页的记录列表
        return new PageResult(total, records);
    }


    /**
     * 购物
     * @param seckillCreateDTO 购买信息
     * @return
     */
    @Override
    public Result<String> purchaseProduct(SeckillCreateDTO seckillCreateDTO) {
        Integer userId = seckillCreateDTO.getUserId();
        Integer seckillId = seckillCreateDTO.getSeckillId();
        Integer quantity = seckillCreateDTO.getPurchaseQuantity();
        String orderId = UUID.randomUUID().toString();

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
        //商品下单
        int userId = BaseContext.getCurrentId();
        Order order=Order.builder()
                .userId(userId)
                .productId(product.getProductId())
                .paymentStatus("未支付")
                .build();
        mallSender.sendOrderMessage(order);

    }


}
