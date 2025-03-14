package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostDTO implements Serializable {
    private Integer userId;
    private String title;
    private String content;
}





