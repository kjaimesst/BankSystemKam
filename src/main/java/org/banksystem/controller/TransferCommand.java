package org.banksystem.controller;

import java.math.BigDecimal;

public class TransferCommand implements Command {
    private final BankFacade facade;
    private final String fromEmail;
    private final String toEmail;
    private final BigDecimal amount;

    public TransferCommand(BankFacade facade, String fromEmail, String toEmail, BigDecimal amount) {
        this.facade = facade;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.amount = amount;
    }

    @Override
    public void execute() {
        facade.transfer(fromEmail, toEmail, amount);
    }

    @Override
    public String toString() {
        return "TransferCommand{" + fromEmail + "->" + toEmail + ", $" + amount + "}";
    }
}
