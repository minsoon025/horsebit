package com.a406.horsebit.service;

import java.time.LocalDateTime;

public interface InitiatorService {
    void resetOrder();
    void resetTokens(Long tokenNo, LocalDateTime initialTime, Long price);
    void resetTokens(Long tokenNo);
    void resetPrice(Long tokenNo, Long price);
    void resetPrice(Long tokenNo);
}
