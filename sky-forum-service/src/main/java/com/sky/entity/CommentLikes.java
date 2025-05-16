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
public class CommentLikes implements Serializable {

    private static final long serialVersionUID = 1L;

    private  Integer likeCommentId;

    private Integer commentId;

    private Integer userId;

    private LocalDateTime createTime;

}
