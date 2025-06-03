package com.sky.vo;

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
public class AppointVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer appointmentId;
    private Integer userId;
    private LocalDateTime visitDate;
    private String visitReason;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String phoneNumber;
    private String username;
}