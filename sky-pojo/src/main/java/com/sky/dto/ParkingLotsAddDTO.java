package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParkingLotsAddDTO implements Serializable {
    private int parkingLotId;
    private int account;

}
