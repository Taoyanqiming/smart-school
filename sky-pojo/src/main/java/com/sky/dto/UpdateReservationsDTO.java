package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Builder
public class UpdateReservationsDTO implements Serializable {
    private Integer reservationId;
    private Integer parkingLotId;
    private Integer spaceId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private String status; // 'active', 'completed', 'cancelled'
}
