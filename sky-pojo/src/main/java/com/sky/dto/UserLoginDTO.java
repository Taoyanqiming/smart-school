package com.sky.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class UserLoginDTO  implements Serializable {

    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ApiModelProperty("密码")
    private String password;

}