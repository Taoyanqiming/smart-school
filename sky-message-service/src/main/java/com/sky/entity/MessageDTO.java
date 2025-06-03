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
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    //接受消息人
    private Integer receiver;
    private Integer sender;
    private Integer type;
    private String sourceModule;
    private Integer sourceId;
    private String content;
    private LocalDateTime createTime;
}