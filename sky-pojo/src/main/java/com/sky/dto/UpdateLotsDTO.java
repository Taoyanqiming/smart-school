package com.sky.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class UpdateLotsDTO implements Serializable {
    private int parkingLotId;
    private String name;
    private String location;
    private int totalSpaces;
    private int availableSpaces;
    private String pricePerHour;

}
