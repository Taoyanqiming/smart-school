package com.sky.service;

import com.sky.dto.AppointmentDTO;
import com.sky.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    /**
     * 用户提交预约申请
     * @param appointmentDTO 预约信息
     */
    void submit(AppointmentDTO appointmentDTO);

    /**
     * 修改预约信息
     * @param appointmentDTO 预约信息
     */
    void update(AppointmentDTO appointmentDTO);

    /**
     * 管理员审核预约
     * @param appointmentId 预约ID
     * @param status 审核状态
     * @param approverId 审核人ID
     */
    void approve(Integer appointmentId, Integer status, Integer approverId);

    /**
     * 根据用户ID查询预约记录
     * @param userId 用户ID
     * @return 预约列表
     */
    List<Appointment> getByUserId(Integer userId);

    /**
     * 根据ID查询预约
     * @param appointmentId 预约ID
     * @return 预约信息
     */
    Appointment getById(Integer appointmentId);
}