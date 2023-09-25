package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RList;

@Getter
@Setter
public class OrderPage {
    private Double volume;
    private RList<OrderSummary> orderSummaryRList;
}
