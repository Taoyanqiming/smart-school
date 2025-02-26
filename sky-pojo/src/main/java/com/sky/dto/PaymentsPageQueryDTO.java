package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentsPageQueryDTO implements Serializable {
    // 手机号码
    private String phoneNumber;
    //支付账单状态
    private String status;
    // 当前页码
    private int page;
    // 每页显示记录数
    private int pageSize;
}
