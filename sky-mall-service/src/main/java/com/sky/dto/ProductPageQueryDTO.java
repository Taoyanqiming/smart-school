package com.sky.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductPageQueryDTO {

    private int page;
    private  int pageSize;
    private String productName;
}
