package com.sky.feign;

import com.sky.dto.OrderSearchDTO;
import com.sky.dto.SeckillCreateDTO;
import com.sky.entity.Order;
import com.sky.entity.UserSeckillRecord;
import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="sky-order-service")
public interface OrderFeignService { // 注意：这里应该是interface而不是class
    /**
     * 查询订单
     */
    @PostMapping("/order/search")
    UserSeckillRecord searchOrders(@RequestBody OrderSearchDTO orderSearchDTO); // 添加分号;

    @PostMapping("/order/create/seckill")
    Result createSeck(@RequestBody SeckillCreateDTO seckillCreateDTO);

    @PutMapping("/order/update/seckill")
    Result updateSeck(@RequestBody SeckillCreateDTO seckillCreateDTO);
}