package com.a406.horsebit.constant;

import com.a406.horsebit.domain.redis.CandleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandleConstant {
    public static final List<CandleType> CANDLE_TYPE_LIST = new ArrayList<CandleType>(Arrays.asList(
            new CandleType("1_MINUTE",  1L),
            new CandleType("3_MINUTE",  3L),
            new CandleType("5_MINUTE",  5L),
            new CandleType("15_MINUTE", 15L),
            new CandleType("1_HOUR",    60L),
            new CandleType("4_HOUR",    240L),
            new CandleType("12_HOUR",   720L),
            new CandleType("1_DAY",     1440L),
            new CandleType("7_DAY",     1440L)
    ));
}
