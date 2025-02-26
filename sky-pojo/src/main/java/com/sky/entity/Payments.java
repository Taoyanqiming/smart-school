package com.sky.entity;

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
public class Payments implements Serializable {
    private Integer paymentId;
    private Integer userId;
    private Integer reservationId;
    private Double amount;
    private String status;
    private String paymentMethod;

}
