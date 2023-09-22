package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Order {
    // use orderNo as score in map
    // use userNo as name of map
    private Long hrNo;
    private Long tokenNo;
    private int price;
    private double quantity;
    private double remain;
    private Timestamp orderTime;
    private String sellBuyFlag;
}
