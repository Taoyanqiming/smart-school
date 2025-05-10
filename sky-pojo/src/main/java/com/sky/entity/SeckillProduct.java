package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer seckillId;
    private Integer productId;
    private BigDecimal seckillPrice;
    private LocalDateTime seckillStartTime;
    private LocalDateTime seckillEndTime;
    private Integer seckillStock;
    private Integer seckillLimit;
    private String seckillImageUrl;
}