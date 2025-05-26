package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageQueryDTO {

    private Integer postId;
    // 当前页码
    private int page;

    // 每页显示记录数
    private int pageSize;
}
