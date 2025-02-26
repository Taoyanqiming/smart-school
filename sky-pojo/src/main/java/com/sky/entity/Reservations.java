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
public class Reservations implements Serializable {
    private Integer reservationId;
    private Integer userId;
    private Integer parkingLotId;
    private Integer spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalFee;
    private String status; // 'active', 'completed', 'cancelled'
}

