package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer favoriteId;
    private Integer postId;
    private Integer userId;
}