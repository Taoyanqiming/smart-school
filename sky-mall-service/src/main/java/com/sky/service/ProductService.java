package com.sky.service;


import com.sky.dto.ProductPageQueryDTO;
import com.sky.dto.PurchaseDTO;
import com.sky.dto.SeckillCreateDTO;
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

}