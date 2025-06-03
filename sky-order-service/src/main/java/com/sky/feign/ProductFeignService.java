package com.sky.feign;

import com.sky.entity.SeckillProduct;
import com.sky.entity.UserSeckillRecord;
import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name="sky-mall-service")
public interface ProductFeignService { // 注意：这里应该是interface而不是class
    /**
     * 查询秒杀商品
     */
    @GetMapping("/mall/seckill/list")
    Result<SeckillProduct> getSeckillProducts(Integer seckillId);

    @PutMapping("/mall/product/update")
    Result updateProducts(Integer productId, Integer account);
    @PutMapping("/mall/seckill/update")
    Result updateSecProducts(Integer seckillId, Integer account);

}