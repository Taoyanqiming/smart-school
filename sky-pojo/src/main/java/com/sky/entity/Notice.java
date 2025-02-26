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
public class Notice implements Serializable {
    private Integer noticeId;
    private Integer userId;
    private String title;
    private String content;
    private LocalDateTime createdTime;
}
