package com.a406.horsebit.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class OrderSummary {
    // use price as score in map
    private Long orderNo;
    private Long userNo;
    private double quantity;
    private double remain;
}
