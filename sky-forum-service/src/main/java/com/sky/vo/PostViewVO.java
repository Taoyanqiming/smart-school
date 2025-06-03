package com.sky.vo;

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
public class PostViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer postId;
    private String title;
    private LocalDateTime createTime;
    private Integer view;

}