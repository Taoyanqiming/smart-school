package com.sky.controller.user;

import com.sky.dto.PaymentsPageQueryDTO;
import com.sky.entity.Payments;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.FeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/pay")
@Slf4j
@Api(tags = "用户车位费用相关接口")
public class PayController {
    @Autowired
    private FeeService feeService;
    /**
     * 查询个人缴费记录(有条件筛选）
     */
    @GetMapping("/page/payments")
    @ApiOperation("查询缴费记录")
    public Result<PageResult> page(PaymentsPageQueryDTO paymentsPageQueryDTO){
        PageResult pageResult = feeService.pageQuery(paymentsPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 缴费功能（将状态设置为已经缴费）
     *
     */
    @PutMapping("/status")
    public void updatePaymentStatus(@RequestBody Payments payments) {
        feeService.updatePaymentStatus(payments);
    }
}
