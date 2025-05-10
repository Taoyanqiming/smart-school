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
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private Integer parentId;
    private Integer answerId;
    private String content;
}