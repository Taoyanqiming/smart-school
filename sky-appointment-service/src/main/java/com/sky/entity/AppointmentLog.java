package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer logId;
    private Integer appointmentId;
    private Integer approverId;
    private Integer approveStatus;
    private LocalDateTime approveTime;
    private LocalDateTime updateTime;
}
