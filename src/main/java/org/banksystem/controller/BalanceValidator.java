package org.banksystem.controller;

import java.math.BigDecimal;
import org.banksystem.model.Customer;

public class BalanceValidator extends TransactionHandler {
    @Override
    public boolean handle(Customer customer, BigDecimal amount) {
        if (customer == null) {
            System.out.println("Validación: cliente inexistente");
            return false;
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Validación: monto inválido");
            return false;
        }
        BigDecimal balance = customer.getAccount().getBalance();
        if (balance.compareTo(amount) < 0) {
            System.out.println("Validación: fondos insuficientes. /nSaldo: $" + balance);
            return false;
        }
        return next == null || next.handle(customer, amount);
    }
}
