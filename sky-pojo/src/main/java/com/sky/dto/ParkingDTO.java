package com.sky.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ParkingDTO implements Serializable {
    private Integer parkingLotId;
    private Integer type;
    private Integer status;
}
