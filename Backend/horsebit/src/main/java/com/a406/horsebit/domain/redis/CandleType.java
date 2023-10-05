package com.a406.horsebit.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CandleType {
    private String candleType;
    private Long candleMinuteTime;
}
