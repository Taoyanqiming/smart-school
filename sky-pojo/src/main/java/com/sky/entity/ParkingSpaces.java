package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpaces implements Serializable {
    private Integer spaceId;
    private Integer parkingLotId;
    private String pricePerHour;
    private String type;
    private String status;


}
