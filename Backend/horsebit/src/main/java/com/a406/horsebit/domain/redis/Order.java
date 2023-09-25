package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Order {
    /*
    userNo as name of map
    tokenNo, orderNo as score in map
     */
    private int price;
    private double quantity;
    private double remain;
    private Timestamp orderTime;
    private String sellBuyFlag;
}
