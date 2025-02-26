package com.sky.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FeedBacksDTO implements Serializable {

    private Integer userId;
    private String feedbackText;
    private String feedbackTitle;
    private String type;
}