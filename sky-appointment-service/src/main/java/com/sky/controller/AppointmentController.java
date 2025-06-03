package com.sky.controller;

import com.github.pagehelper.PageHelper;
import com.sky.dto.AppointmentDTO;
import com.sky.entity.Appointment;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/appoint")
@Api(tags = "预约相关接口")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 用户提交预约申请
     * @param appointmentDTO 预约信息
     * @return 操作结果
     */
    @PostMapping("/submit")
    @ApiOperation("提交预约申请")
    public Result<Integer> submit(@RequestBody AppointmentDTO appointmentDTO) {
            Integer appointmentId = appointmentService.submit(appointmentDTO);
            return Result.success(appointmentId);

    }

    /**
     * 修改预约信息
     * @param appointmentDTO 预约信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @ApiOperation("修改预约信息")
    public Result update(@RequestBody AppointmentDTO appointmentDTO, HttpServletRequest request) {
        appointmentDTO.setUserId(Integer.valueOf( request.getHeader("X-User-Id")));
        appointmentService.update(appointmentDTO);
        return Result.success();
    }

    /**
     * 管理员审核预约
     * @param appointmentId 预约ID
     * @param status 审核状态
     * @return 操作结果
     */
    @PutMapping("/approve/{appointmentId}/{status}")
    @ApiOperation("审核预约")
    public Result approve(@PathVariable Integer appointmentId, @PathVariable Integer status, HttpServletRequest request) {
        // 从ThreadLocal中获取当前登录管理员ID
        Integer approverId = Integer.valueOf( request.getHeader("X-User-Id"));
        appointmentService.approve(appointmentId, status, approverId);
        return Result.success();
    }

    /**
     * 根据条件查询预约记录
     * @return 预约列表
     */
    @PostMapping("/list")
    @ApiOperation("查询用户预约列表")
    public Result<List<Appointment>> list(@RequestBody AppointmentDTO appointmentDTO) {
        List<Appointment> appointments =appointmentService.getByUserId(appointmentDTO);
        return Result.success(appointments);
    }

    /**
     * 根据ID查询预约
     * @param appointmentId 预约ID
     * @return 预约信息
     */
    @GetMapping("/{appointmentId}")
    @ApiOperation("查询预约详情")
    public Result<Appointment> getById(@PathVariable Integer appointmentId) {
        Appointment appointment = appointmentService.getById(appointmentId);
        return Result.success(appointment);
    }
}