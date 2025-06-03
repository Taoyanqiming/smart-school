package com.sky.controller.admin;

import com.sky.dto.ProductCreateDTO;

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
@RequestMapping("/admin/mall")
@Api(tags = "管理员商城商品接口")
public class adminMallController {

    @Autowired
    private ProductService productService;

    /**
     * 创建普通商品
     * @param productCreateDTO 包含用户ID、秒杀商品ID（可选）、购买数量
     * @return 操作结果
     */
    @ApiOperation("创建普通商品")
    @PostMapping("/product/create")
    public Result<String> purchaseProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        productService.createProduct(productCreateDTO);
        return Result.success();
    }
    /**
     * 设置限时秒杀
     */
    @ApiOperation("创建秒杀商品")
    @PostMapping("/product/set")
    public Result<String> seckProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        productService.createProduct(productCreateDTO);
        return Result.success();
    }

}