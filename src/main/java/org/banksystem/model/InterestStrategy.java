package org.banksystem.model;

import java.math.BigDecimal;

/**
 * interestStrategy - strategy
 * interfaz para diferentes algoritmos de calculo de interes
 */

public interface InterestStrategy {
    BigDecimal calculateInterest(BigDecimal balance);
}
