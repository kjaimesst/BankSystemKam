package org.banksystem.model;

import java.math.BigDecimal;

/**
 * Display name: InterestStrategy — Strategy
 * Interfaz para diferentes algoritmos de cálculo de interés.
 */
public interface InterestStrategy {
    BigDecimal calculateInterest(BigDecimal balance);
}
