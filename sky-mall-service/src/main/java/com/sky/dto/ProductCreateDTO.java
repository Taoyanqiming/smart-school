package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDTO {
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private Integer productLimit;
    private String productImageUrl;
    private Integer productId;

}
