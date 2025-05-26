package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer appointmentId;
    private Integer userId;
    private LocalDateTime visitDate;
    private String visitReason;
    private Integer status;
}
