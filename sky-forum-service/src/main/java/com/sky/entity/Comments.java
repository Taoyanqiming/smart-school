package com.sky.entity;

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
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private Integer parentId;
    private Integer answerId;
    private Integer liked;
    private Integer status;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}