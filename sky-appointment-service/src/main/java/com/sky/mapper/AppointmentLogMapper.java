package com.sky.mapper;

import com.sky.entity.AppointmentLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentLogMapper {
    /**
     * 插入审批日志
     * @param appointmentLog 审批日志
     */
    void insert(AppointmentLog appointmentLog);
}