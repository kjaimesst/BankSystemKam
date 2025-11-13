package org.banksystem.model;

import java.math.BigDecimal;

/**
 * strategy
 * estrategia estandar como ejem 1%
 */
public class StandardInterest implements InterestStrategy {
    private final BigDecimal rate;

    public StandardInterest() {
        this.rate = new BigDecimal("0.01"); // 1%
    }

    @Override
    public BigDecimal calculateInterest(BigDecimal balance) {
        return balance.multiply(rate);
    }
}

