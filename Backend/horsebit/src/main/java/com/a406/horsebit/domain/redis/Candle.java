package com.a406.horsebit.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class Candle {
    private LocalDateTime startTime;
    private Long open;
    private Long close;
    private Long high;
    private Long low;
    private Double volume;
}