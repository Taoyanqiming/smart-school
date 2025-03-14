package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private Integer userId;

    private String username;

    private String phoneNumber;

    private String email;

    private  String role;



}

