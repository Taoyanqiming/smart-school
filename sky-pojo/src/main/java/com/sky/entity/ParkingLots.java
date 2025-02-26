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
public class ParkingLots implements Serializable {

    private int parkingLotId;
    private String name;
    private String location;
    private int totalSpaces;
    private int availableSpaces;
    private String pricePerHour;

}

