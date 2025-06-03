package com.sky.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 消息实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer messageId;
    private Integer userId;
    private Integer sender;
    //0系统消息，1回复消息，2点赞消息，3订单消息
    private Integer type;
    private String sourceModule;
    private Integer sourceId;
    private String content;
    private Integer isRead;
    private LocalDateTime createTime;
    private Integer deleteFlag;

}
