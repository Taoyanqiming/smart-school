package com.sky.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "查询用户是否点赞收藏")
public class PostStatusVO {
    private boolean isLiked ;
    private boolean isFavorited ;
}
