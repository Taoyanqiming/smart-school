package com.sky.dto;

import lombok.Data;
import java.io.Serializable;


@Data
public class ParkingSpacesQueryDTO implements Serializable {
    private Integer parkingLotId;
    private String type;
    private String status;
    private  int page;
    private  int pageSize;
}
