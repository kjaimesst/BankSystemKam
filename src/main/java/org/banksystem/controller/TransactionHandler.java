package org.banksystem.controller;

import java.math.BigDecimal;
import org.banksystem.model.Customer;

/**
 * Chain of Responsibility: base para validadores.
 */
public abstract class TransactionHandler {
    protected TransactionHandler next;

    public void setNext(TransactionHandler next) {
        this.next = next;
    }

    public abstract boolean handle(Customer customer, BigDecimal amount);
}
