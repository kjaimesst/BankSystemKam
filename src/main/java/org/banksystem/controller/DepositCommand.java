package org.banksystem.controller;

import java.math.BigDecimal;

public class DepositCommand implements Command {
    private final BankFacade facade;
    private final String email;
    private final BigDecimal amount;

    public DepositCommand(BankFacade facade, String email, BigDecimal amount) {
        this.facade = facade;
        this.email = email;
        this.amount = amount;
    }

    @Override
    public void execute() {
        facade.deposit(email, amount);
    }

    @Override
    public String toString() {
        return "DepositCommand{" + email + ", $" + amount + "}";
    }
}
