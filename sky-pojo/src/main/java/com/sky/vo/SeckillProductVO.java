package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer seckillId;
    private Integer productId;
    private BigDecimal seckillPrice;
    private Date seckillStartTime;
    private Date seckillEndTime;
    private Integer seckillStock;
    private Integer seckillLimit;
}