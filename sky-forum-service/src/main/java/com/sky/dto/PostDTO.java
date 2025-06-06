package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer postId;
    private Integer userId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}