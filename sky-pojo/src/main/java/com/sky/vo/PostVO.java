package com.sky.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PostVO implements Serializable {
    private Integer postId;
    private Integer userId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
}