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
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer orderId;
    private Integer userId;
    private Integer productId;
    private Integer seckillId;
    private Integer orderQuantity;
    private BigDecimal orderPrice;
    private String orderStatus;
    private LocalDateTime orderTime;
    private LocalDateTime paymentTime;
    private LocalDateTime paymentDeadline;
    private String paymentStatus;
}