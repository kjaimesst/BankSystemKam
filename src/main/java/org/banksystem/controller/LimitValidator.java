package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * patron chain of responsibility: valida que la transacción no supere un monto máximo permitido
 */
public class LimitValidator extends TransactionHandler {
    private static final BigDecimal MAX_LIMIT = new BigDecimal("10000000"); // 10 millones

    @Override
    public boolean handle(Account account, BigDecimal amount) {
        if (amount.compareTo(MAX_LIMIT) > 0) {
            account.notifyAllObservers("Operación rechazada: monto supera el límite permitido de " + MAX_LIMIT);
            return false;
        }
        if (nextHandler != null) return nextHandler.handle(account, amount);
        return true;
    }
}
