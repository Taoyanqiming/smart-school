package com.sky.mapper;

import com.github.pagehelper.Page;

import com.sky.dto.ProductPageQueryDTO;
import com.sky.entity.SeckillProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 秒杀商品数据访问层接口
 */
@Mapper
public interface SeckillProductMapper {
    /**
     * 分页获取秒杀商品列表
     * @return 分页秒杀商品列表
     */
    Page<SeckillProduct> pageQuery(ProductPageQueryDTO productPageQueryDTO);

    /**
     * 根据秒杀商品ID获取秒杀商品信息
     * @param seckillId 秒杀商品ID
     * @return 秒杀商品信息
     */
    SeckillProduct getSeckillProductById(Integer seckillId);

    /**
     * 扣减秒杀商品库存
     * @param seckillId 秒杀商品ID
     * @param quantity  扣减数量
     * @return 影响的行数
     */
    int decreaseSeckillStock(Integer seckillId, Integer quantity);

    /**
     * 增加秒杀商品库存
     * @param seckillId 秒杀商品 ID
     * @param quantity 增加数量
     * @return 影响行数
     */
    int increaseSeckillStock(Integer seckillId, Integer quantity);
}