package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * patron chain of responsibility: clase base para manejar validaciones en cadena
 */
public abstract class TransactionHandler {
    protected TransactionHandler nextHandler;

    public void setNextHandler(TransactionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract boolean handle(Account account, BigDecimal amount);
}
