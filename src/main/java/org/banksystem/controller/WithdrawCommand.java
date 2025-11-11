package org.banksystem.controller;

import java.math.BigDecimal;

public class WithdrawCommand implements Command {
    private final BankFacade facade;
    private final String email;
    private final BigDecimal amount;

    public WithdrawCommand(BankFacade facade, String email, BigDecimal amount) {
        this.facade = facade;
        this.email = email;
        this.amount = amount;
    }

    @Override
    public void execute() {
        facade.withdraw(email, amount);
    }

    @Override
    public String toString() {
        return "WithdrawCommand{" + email + ", $" + amount + "}";
    }
}
