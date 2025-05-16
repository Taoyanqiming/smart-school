package com.sky.service;


import com.sky.dto.PurchaseDTO;
import com.sky.entity.Product;
import com.sky.entity.SeckillProduct;
import com.sky.result.PageResult;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {
//    /**
//     * 商品分页查询
//     * @param productPageQueryDTO
//     * @return
//     */
//    PageResult pageQuery(ProductPageQueryDTO productPageQueryDTO);
//
//    /**
//     * 获取秒杀商品列表
//     * @return 秒杀商品列表
//     */
//    PageResult getSeckillProducts(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 购买商品
     * @param purchaseDTO 购买信息
     * @return 购买是否成功
     */
    boolean purchaseProduct(PurchaseDTO purchaseDTO);

    void purchase(PurchaseDTO purchaseDTO);
//    /**
//     * 模拟支付功能
//     * @param orderId 订单 ID
//     * @return 支付是否成功
//     */
//    boolean simulatePayment(Integer orderId);
//
//    /**
//     * 退款功能
//     * @param orderId 订单 ID
//     * @return 退款是否成功
//     */
//    boolean refundOrder(Integer orderId);
//
//    /**
//     * 取消超时未支付的订单
//     */
//    void cancelExpiredOrders();
}