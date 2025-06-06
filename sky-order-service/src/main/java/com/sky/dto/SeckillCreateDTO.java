package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeckillCreateDTO {
    private String recordId;
    private Integer userId;
    private Integer seckillId;
    private Integer purchaseQuantity;

}
