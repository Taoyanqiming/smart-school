package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private Integer productLimit;
}