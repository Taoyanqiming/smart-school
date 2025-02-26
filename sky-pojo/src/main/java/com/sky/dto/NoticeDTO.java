package com.sky.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Builder
@Data
public class NoticeDTO implements Serializable {

    private Integer userId;
    private String title;
    private String content;


}
