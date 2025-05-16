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
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer postId;

    private Integer userId;

    private String title;

    private String content;

    private LocalDateTime createTime;

    private Integer view;

    private Integer liked;
    private String images;

    private Integer favorite;

    private boolean isLiked ;
    private boolean isFavorited ;

}