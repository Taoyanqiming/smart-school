package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSeckillRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer recordId;
    private Integer userId;
    private Integer seckillId;
    private Integer purchaseQuantity;
}