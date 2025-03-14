package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成全参构造函数
public class SentNoticeDTO implements Serializable {

    private String phoneNumber;
    private String title;
    private String content;
}