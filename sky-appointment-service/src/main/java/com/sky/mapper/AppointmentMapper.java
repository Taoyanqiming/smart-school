package com.sky.mapper;

import com.sky.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppointmentMapper {
    /**
     * 插入预约记录
     * @param appointment 预约信息
     */
    void insert(Appointment appointment);

    /**
     * 更新预约记录
     * @param appointment 预约信息
     */
    void update(Appointment appointment);

    /**
     * 根据ID查询预约
     * @param appointmentId 预约ID
     * @return 预约信息
     */
    Appointment getById(Integer appointmentId);

    /**
     * 根据用户ID查询预约记录
     * @param userId 用户ID
     * @return 预约列表
     */
    List<Appointment> getByUserId(Integer userId);
}