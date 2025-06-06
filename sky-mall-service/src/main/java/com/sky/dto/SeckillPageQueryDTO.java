package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeckillPageQueryDTO {

    private int page;
    private  int pageSize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
