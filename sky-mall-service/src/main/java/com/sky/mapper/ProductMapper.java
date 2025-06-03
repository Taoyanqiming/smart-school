package com.sky.mapper;

import com.github.pagehelper.Page;

import com.sky.dto.ProductPageQueryDTO;
import com.sky.entity.Product;
import com.sky.entity.SeckillProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品数据访问层接口
 */
@Mapper
public interface ProductMapper {
    /**
     * 商品分页查询
     * @param productPageQueryDTO
     * @return
     */
    Page<Product> pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 根据商品ID获取商品信息
     * @param productId 商品ID
     * @return 商品信息
     */
    Product getProductById(Integer productId);

    /**
     * 扣减商品库存
     * @param productId 商品ID
     * @param quantity  扣减数量
     * @return 影响的行数
     */
    int decreaseProductStock(Integer productId, Integer quantity);

    /**
     * 增加商品库存
     * @param productId 商品 ID
     * @param quantity 增加数量
     * @return 影响行数
     */
    int increaseProductStock(Integer productId, Integer quantity);

    /**
     * 管理员创建普通商品
     * @param product
     */
    void insert(Product product);

}