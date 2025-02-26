package com.sky.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "查询订单返回的数据格式")
public class PaymentVO implements Serializable {

    private Integer paymentId;
    private String username;
    private String phoneNumber;
    private Integer amount;
    private String status;
    private String paymentMethod;
    private LocalDateTime paymentTime;
}
