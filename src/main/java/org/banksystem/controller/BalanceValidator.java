package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * Display name: BalanceValidator — Chain of Responsibility
 * Valida que la cuenta tenga fondos suficientes.
 */
public class BalanceValidator extends TransactionHandler {

    @Override
    public boolean handle(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            account.notifyAllObservers("Operación rechazada: fondos insuficientes.");
            return false;
        }
        if (nextHandler != null) return nextHandler.handle(account, amount);
        return true;
    }
}

