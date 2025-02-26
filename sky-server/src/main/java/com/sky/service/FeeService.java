package com.sky.service;

import com.sky.dto.PaymentsPageQueryDTO;
import com.sky.entity.Payments;
import com.sky.result.PageResult;

public interface FeeService {
    /**
     * 分页展示支付账单记录
     * @param paymentsPageQueryDTO
     * @return
     */
    PageResult pageQuery(PaymentsPageQueryDTO paymentsPageQueryDTO);

    /**
     * 创建账单
     * @param payments
     */
    void createPay(Payments payments);
}
