package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSummary {
    // use price as score in map
    private Long orderNo;
    private Long userNo;
    private double quantity;
    private double remain;
}
