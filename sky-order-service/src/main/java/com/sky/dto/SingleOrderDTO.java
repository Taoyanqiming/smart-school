package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SingleOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  orderId;
    private Integer userId;

}