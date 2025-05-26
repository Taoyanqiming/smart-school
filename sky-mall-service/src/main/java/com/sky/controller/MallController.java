package com.sky.controller;

import com.sky.dto.ProductPageQueryDTO;
import com.sky.dto.PurchaseDTO;
import com.sky.dto.SeckillCreateDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品相关操作的控制器
 */
@RestController
@RequestMapping("/mall")
@Api(tags = "商城商品接口")
public class MallController {

    @Autowired
    private ProductService productService;
/**
 * 搜索
 */

    /**
     * 展示全部商品
     * @return 全部商品列表
     */
    @ApiOperation("分页查询商品列表")
    @GetMapping("/product/list")
    public Result<PageResult> getAllProducts( ProductPageQueryDTO productPageQueryDTO) {
        PageResult products = productService.pageQuery(productPageQueryDTO);
        return Result.success(products);
    }

    /**
     * 展示秒杀商品
     * @return 秒杀商品列表
     */
    @ApiOperation("分页查询秒杀商品列表")
    @GetMapping("/seckill/list")
    public Result<PageResult> getSeckillProducts(ProductPageQueryDTO productPageQueryDTO) {
        PageResult seckillProducts = productService.getSeckillProducts(productPageQueryDTO);
        return Result.success(seckillProducts);
    }

    /**
     * 购买秒杀商品
     * @param seckillCreateDTO 包含用户ID、秒杀商品ID（可选）、购买数量
     * @return 操作结果
     */
    @ApiOperation("购买秒杀商品")
    @PostMapping("/purchase/seckill")
    public Result<String> purchaseProduct(@RequestBody SeckillCreateDTO seckillCreateDTO) {
        return productService.purchaseProduct(seckillCreateDTO); // 直接返回 Service 的 Result 对象
    }
    /**
     * 购买普通商品
     * @param purchaseDTO 包含用户ID、秒杀商品ID（可选）、购买数量
     * @return 操作结果
     */
    @ApiOperation("购买普通商品")
    @PostMapping("/purchase/normal")
    public Result<String> purchaseProduct(@RequestBody PurchaseDTO purchaseDTO) {
        productService.purchaseNormalProduct(purchaseDTO);
        return Result.success();
    }



}