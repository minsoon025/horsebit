package com.a406.horsebit.domain.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
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

    public Order(long price, double quantity, double remain, String sellBuyFlag) {
        this.price = price;
        this.quantity = quantity;
        this.remain = remain;
        this.sellBuyFlag = sellBuyFlag;
    }
}
