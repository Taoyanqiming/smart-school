package com.sky.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "反馈查询返回的数据格式")
public class FeedBacksVO implements Serializable {
    private Integer feedbackId;
    private String title;
    private String content;
    private String username;
    private String phoneNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime feedbackTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;
    private String replyPerson;
    private String status; // 使用整数表示状态
    private String type; // 使用整数表示类型
}