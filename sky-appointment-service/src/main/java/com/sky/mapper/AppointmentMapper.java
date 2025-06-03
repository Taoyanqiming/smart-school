package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.AppointmentDTO;
import com.sky.entity.Appointment;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppointmentMapper {
    /**
     * 插入预约记录
     * @param appointment 预约信息
     */
    @AutoFill(OperationType.INSERT)
    void insert(Appointment appointment);

    /**
     * 更新预约记录
     * @param appointment 预约信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Appointment appointment);

    /**
     * 根据ID查询预约
     * @param appointmentId 预约ID
     * @return 预约信息
     */
    Appointment getById(Integer appointmentId);

    /**
     * 根据条件查询预约记录
     * @param appointmentDTO
     * @return 预约列表
     */
    List<Appointment> getAppoint(AppointmentDTO appointmentDTO);
}