package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Candle {
    private Timestamp startTime;
    private Long open;
    private Long close;
    private Long high;
    private Long low;
    private Double volume;
}