package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Order {
    /*
    userNo as name of map
    tokenNo, orderNo as score in map
     */
    private long price;
    private double quantity;
    private double remain;
    private LocalDateTime orderTime;
    private String sellBuyFlag;
}
