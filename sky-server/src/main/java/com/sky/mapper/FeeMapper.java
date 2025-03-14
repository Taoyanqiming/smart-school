package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.PaymentsPageQueryDTO;
import com.sky.entity.Payments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeeMapper {
    /**
     * 分页展示支付账单记录
     * @param paymentsPageQueryDTO
     * @return
     */
    Page<Payments> selectPayments(PaymentsPageQueryDTO paymentsPageQueryDTO);

    /**
     * 创建支付账单
     * @param payments
     */
    void createPay(Payments payments);

    void updatePaymentStatus(Payments payments);
}
