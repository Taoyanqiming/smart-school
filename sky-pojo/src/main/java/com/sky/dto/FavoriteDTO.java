package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FavoriteDTO implements Serializable {
    private Integer postId;
    private Integer userId;
}