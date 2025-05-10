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
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer messageId;
    private Integer userId;
    private Integer postId;
    private Integer commentId;
    private Integer likeCommentId;
    private Integer likeId;
    private Integer favoriteId;
    private String messageType;
    private String messageContent;
    private Boolean isRead;
    private LocalDateTime createdTime;
}
