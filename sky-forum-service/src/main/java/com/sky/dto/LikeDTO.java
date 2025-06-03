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
public class LikeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer postId;
    //点赞人
    private Integer userId;
}