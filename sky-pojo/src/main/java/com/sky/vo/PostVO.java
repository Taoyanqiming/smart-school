package com.sky.vo;

import com.sky.entity.Posts;
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
public class PostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer postId;
    private Integer userId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private boolean isLiked;
    private boolean isFavorited;

}