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
public class Replies implements Serializable {
    private Integer replyId;
    private Integer feedbackId;
    private Integer userId;
    private String replyText;
    private LocalDateTime replyTime;
}
