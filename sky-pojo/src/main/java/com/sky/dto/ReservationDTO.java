package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReservationDTO implements Serializable {
    private String phoneNumber;
    private int parkingLotId;
    private int spaceId;
    private String licensePlate; // 新增车牌号字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;  // 前端传来的预约开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}
