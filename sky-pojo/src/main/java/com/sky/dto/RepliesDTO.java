package com.sky.dto;

import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RepliesDTO implements Serializable {
    private Integer feedbackId;
    private String replyText;
    private Integer userId;
}
