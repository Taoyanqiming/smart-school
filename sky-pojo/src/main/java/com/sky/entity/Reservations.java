package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Reservations implements Serializable {
    private Integer reservationId;
    private Integer userId;
    private Integer parkingLotId;
    private Integer spaceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private double totalFee;
    private String status; // 'active', 'completed', 'cancelled'
    private String licensePlate; // 新增车牌号字段
}

