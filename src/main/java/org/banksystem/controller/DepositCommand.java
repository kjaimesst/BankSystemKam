package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * Display name: DepositCommand — Command
 * Encapsula una operación de depósito.
 */
public class DepositCommand implements Command {
    private final Account account;
    private final BigDecimal amount;

    public DepositCommand(Account account, BigDecimal amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        account.deposit(amount);
    }
}

