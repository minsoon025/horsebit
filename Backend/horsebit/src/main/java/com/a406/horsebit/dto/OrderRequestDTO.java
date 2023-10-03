package com.a406.horsebit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {
    private Long tokenNo;
    private Double volume;
    private Long price;
}
