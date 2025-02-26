package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.PaymentsPageQueryDTO;
import com.sky.entity.Payments;
import com.sky.mapper.FeeMapper;
import com.sky.result.PageResult;
import com.sky.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeeServiceImpl implements FeeService {
    @Autowired
    private FeeMapper feeMapper;
    /**
     * 分页展示支付账单记录
     * @param paymentsPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(PaymentsPageQueryDTO paymentsPageQueryDTO){
        PageHelper.startPage(paymentsPageQueryDTO.getPage(), paymentsPageQueryDTO.getPageSize());
        Page<Payments> page = feeMapper.selectPayments(paymentsPageQueryDTO);
        long total = page.getTotal();
        List<Payments> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 创建账单
     * @param payments
     */
    @Override
    public void createPay(Payments payments){
        feeMapper.createPay(payments);

    }
}
