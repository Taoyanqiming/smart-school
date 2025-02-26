package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class FeedBacksPageQueryDTO implements Serializable {
    private String status;
    private String type;
    private  int page;
    private  int pageSize;
}
