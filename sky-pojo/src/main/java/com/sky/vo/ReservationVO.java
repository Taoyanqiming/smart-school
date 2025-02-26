package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "返回根据手机号查询的停车位预约信息")
public class ReservationVO implements Serializable {
    @ApiModelProperty("预约表ID")
    private Integer reservationId;
    @ApiModelProperty("用户姓名")
    private String username;
    @ApiModelProperty("用户手机号")
    private String phoneNumber;
    @ApiModelProperty("停车场名称")
    private String name;
    @ApiModelProperty("停车场位置")
    private String location;
    @ApiModelProperty("车位ID")
    private Integer spaceId;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("总共费用")
    private double totalFee;
    @ApiModelProperty("预约状态")
    private String status;
}
