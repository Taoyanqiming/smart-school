package com.sky.controller.admin;

import com.sky.dto.PaymentsPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.FeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/pay")
@Slf4j
@Api(tags = "车位费用相关接口")
public class FeeController {
    @Autowired
    private FeeService feeService;

    /**
     *查询所有用户缴费记录（选择待缴费和已经缴费）
     */
    @GetMapping("/page/payments")
    @ApiOperation("查询缴费记录")
    public Result<PageResult> page(PaymentsPageQueryDTO paymentsPageQueryDTO){
        log.info("支付记录分页查询为:{}", paymentsPageQueryDTO);
        PageResult pageResult = feeService.pageQuery(paymentsPageQueryDTO);
        return Result.success(pageResult);
    }
}
