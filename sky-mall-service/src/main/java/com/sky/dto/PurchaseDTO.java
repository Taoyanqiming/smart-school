package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PurchaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer productId;
    private Integer quantity;
}