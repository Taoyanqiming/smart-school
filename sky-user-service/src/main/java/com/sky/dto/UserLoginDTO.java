package com.sky.dto;


import lombok.Data;

import java.io.Serializable;

@Data

public class UserLoginDTO  implements Serializable {

    private String phoneNumber;
    private String password;

}