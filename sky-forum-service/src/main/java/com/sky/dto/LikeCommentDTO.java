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
public class LikeCommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private  Integer likeCommentId;
    private Integer commentId;
    //评论人
    private Integer userId;
}
