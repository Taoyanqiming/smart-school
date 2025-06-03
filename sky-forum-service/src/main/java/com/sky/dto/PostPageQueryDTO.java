package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPageQueryDTO {
    // 帖子标题（模糊查询）
    private String title;

    // 起始时间（查询创建时间 >= 该时间的帖子）
    private LocalDateTime startTime; // 使用 LocalDateTime 匹配数据库 timestamp

    // 结束时间（查询创建时间 <= 该时间的帖子）
    private LocalDateTime endTime;

    // 排序类型（可选值：newest/mostViewed）
    private String sortType; // 对应 XML 中的 sortType 判断

    // 当前页码
    private int page;
    // 每页显示记录数
    private int pageSize;
}
