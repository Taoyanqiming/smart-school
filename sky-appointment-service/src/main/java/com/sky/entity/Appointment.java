package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Long appointmentId;
    private Long userId;
    private Date visitDate;
    private String visitReason;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
