package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  orderId;
    private Integer userId;
    private Integer productId;
    private Integer seckillId;
    private Integer orderQuantity;
    private BigDecimal orderPrice;
    private String orderStatus;
    private LocalDateTime createTime;
    private LocalDateTime paymentTime;

}