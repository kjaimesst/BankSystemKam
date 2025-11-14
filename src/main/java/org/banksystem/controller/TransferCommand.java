package org.banksystem.controller;

import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * patron command encapsula una transferencia entre cuentas
 * */

public class TransferCommand implements Command {

    private final Account origin;
    private final Account destination;
    private final BigDecimal amount;

    public TransferCommand(Account origin, Account destination, BigDecimal amount) {
        this.origin = origin;
        this.destination = destination;
        this.amount = amount;
    }

    @Override
    public void execute() {
        origin.transfer(amount, destination);
    }
}
