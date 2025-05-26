package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPageQueryDTO {
    // 当前页码
    private int page;
    // 每页显示记录数
    private int pageSize;
    private String keyword;
    private String sortType;
    private LocalDate startDate; // 开始日期
    private LocalDate endDate;   // 结束日期
}
