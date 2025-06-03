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
public class CommentPageQueryDTO implements Serializable {
    // 帖子ID（必传）
    private Long postId;
    private Integer userId;
    // 当前页码
    private int page;
    // 每页显示记录数
    private int pageSize;
}