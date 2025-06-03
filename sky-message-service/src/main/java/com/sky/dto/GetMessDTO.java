package com.sky.dto;

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
public class GetMessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;
    //0系统消息，1回复消息，2点赞消息，3订单消息
    private Integer type;
    private Integer deleteFlag;
    // 当前页码
    private int page;

    // 每页显示记录数
    private int pageSize;

}