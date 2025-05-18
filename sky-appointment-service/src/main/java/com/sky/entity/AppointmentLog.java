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
public class AppointmentLog {
    private Long logId;
    private Long appointmentId;
    private Long approverId;
    private Integer approveStatus;
    private Date approveTime;
    private Date updateTime;
}
