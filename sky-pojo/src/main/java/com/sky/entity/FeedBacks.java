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
public class FeedBacks implements Serializable {
    private Integer feedbackId;
    private Integer userId;
    private String feedbackText;
    private String feedbackTitle;
    private String status; // 使用整数表示状态
    private String type; // 使用整数表示类型
    private LocalDateTime feedbackTime;
}
