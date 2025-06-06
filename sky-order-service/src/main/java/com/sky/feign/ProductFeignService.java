package com.sky.feign;

import com.sky.entity.SeckillProduct;
import com.sky.entity.UserSeckillRecord;
import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name="mall-service")
public interface ProductFeignService { // 注意：这里应该是interface而不是class
    /**
     * 查询秒杀商品
     */
    @GetMapping("/mall/seckill/info")
    Result<SeckillProduct> getSeckillProducts(@RequestParam Integer seckillId);

    @PutMapping("/mall/decrease/product")
    Result decreaseRepor(@RequestParam Integer seckillId,@RequestParam Integer productId,@RequestParam Integer account);
    @PutMapping("/mall/return/product")
    Result ReturnRepor(@RequestParam Integer seckillId,@RequestParam Integer productId,@RequestParam Integer account);

}