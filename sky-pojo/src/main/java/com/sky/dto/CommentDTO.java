package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private Integer postId;
    private Integer userId;
    private Integer parentCommentId;
    private String content;
}
