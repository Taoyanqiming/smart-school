package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageDTO {
    private Integer userId;
    private String orderStatus;
    private LocalDateTime createTime;
    private int page;
    private  int pageSize;
}
