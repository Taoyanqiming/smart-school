package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppointmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer appointmentId;
    private Integer userId;
    private LocalDateTime visitDate;
    private String visitReason;
    //0=待审批，1=已通过，2=已拒绝
    private Integer status;

}
