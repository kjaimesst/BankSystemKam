package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * chain of Responsibility: valida que la cuenta tenga fondos suficientes
 */
public class BalanceValidator extends TransactionHandler {

    @Override
    public boolean handle(Account account, BigDecimal amount) {
        // Si el monto es negativo → es un retiro/transferencia
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal absAmount = amount.abs();
            if (account.getBalance().compareTo(absAmount) < 0) {
                account.notifyAllObservers("Operación rechazada: fondos insuficientes.");
                return false;
            }
        }

        if (nextHandler != null) return nextHandler.handle(account, amount);
        return true;
    }

}
