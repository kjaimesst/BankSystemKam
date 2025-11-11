package org.banksystem.controller;

import java.math.BigDecimal;
import org.banksystem.model.Customer;

public class LimitValidator extends TransactionHandler {
    private final BigDecimal limit;

    public LimitValidator(double limit) {
        this.limit = BigDecimal.valueOf(limit);
    }

    @Override
    public boolean handle(Customer customer, BigDecimal amount) {
        if (amount.compareTo(limit) > 0) {
            System.out.println("Validación: monto supera el límite permitido de $" + limit);
            return false;
        }
        return next == null || next.handle(customer, amount);
    }
}
