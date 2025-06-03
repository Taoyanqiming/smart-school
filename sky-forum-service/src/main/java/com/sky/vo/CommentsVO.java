package com.sky.vo;

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
public class CommentsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer commentId;
    private Integer postId;
    private String userName;
    private String icon;
    private Integer userId;
    private Integer parentId;
    private Integer answerId;
    private Integer liked;
    private Integer status;
    private String content;
    private LocalDateTime createTime;
    private boolean userIsLiked ;


}
