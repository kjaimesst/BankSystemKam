package org.banksystem.model;

import java.math.BigDecimal;

/**
 * Display name: PremiumInterest — Strategy
 * Estrategia premium (por ejemplo 3%).
 */
public class PremiumInterest implements InterestStrategy {
    private final BigDecimal rate;

    public PremiumInterest() {
        this.rate = new BigDecimal("0.03"); // 3%
    }

    @Override
    public BigDecimal calculateInterest(BigDecimal balance) {
        return balance.multiply(rate);
    }
}
