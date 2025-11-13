package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * command: encapsula una operación de retiro
 */
public class WithdrawCommand implements Command {
    private final Account account;
    private final BigDecimal amount;

    public WithdrawCommand(Account account, BigDecimal amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() {
        account.withdraw(amount);
    }
}
