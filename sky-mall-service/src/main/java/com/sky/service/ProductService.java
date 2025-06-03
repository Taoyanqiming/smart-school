package com.sky.service;


import com.sky.dto.*;
import com.sky.entity.Product;
import com.sky.entity.SeckillProduct;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {
    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 获取秒杀商品列表
     * @return 秒杀商品列表
     */
    PageResult getSeckillProducts(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 购买商品
     * @param seckillCreateDTO 购买信息
     * @return 购买是否成功
     */
    Result<String> purchaseProduct(SeckillCreateDTO seckillCreateDTO);

    void purchaseNormalProduct(PurchaseDTO purchaseDTO);

    /**
     * 创建普通产品
     * @param productDTO
     * @return
     */
    Result<Integer> createProduct(ProductCreateDTO productDTO);

    /**
     * 创建秒杀商品
     * @param seckSetDTO
     * @return
     */
    Result<String> publishSeckillProduct(SeckSetDTO seckSetDTO);

    /**
     * 获取单个秒杀商品详情
     * @param seckillId
     * @return
     */
    SeckillProduct getSeckillProductById(Integer seckillId);

    /**
     * 修改普通库存
     * @param productId
     * @param account
     */
    void updateRepertory(Integer productId, Integer account);
    void updateSecRepertory(Integer seckillId, Integer account);
    /**
     * 获取单个product详情
     * @param productId
     * @return
     */
    Product getProductById(Integer productId);
}