package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "筛选停车位返回的数据格式")
public class ParkingVO implements Serializable {
    @ApiModelProperty("停车位ID")
    private Integer spaceId;
    @ApiModelProperty("停车场ID")
    private Integer parkingLotId;
    @ApiModelProperty("每小时收费")
    private String pricePerHour;
    @ApiModelProperty("车位类型")
    private Integer type;
    @ApiModelProperty("车位状态")
    private Integer status;
    @ApiModelProperty("预约使用时间")
    private Timestamp occupiedTime;
}
