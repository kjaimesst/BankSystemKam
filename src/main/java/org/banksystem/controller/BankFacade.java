package org.banksystem.controller;

import java.math.BigDecimal;
import org.banksystem.model.*;

public class BankFacade {
    protected final BankCoreSingleton bankCore = BankCoreSingleton.getInstance();

    public Customer registerCustomer(String name, String email, String accountType) {
        return bankCore.registerCustomer(name, email, accountType);
    }

    // validaciones con Chain of Responsibility
    protected boolean validateForWithdraw(Customer customer, BigDecimal amount) {
        BalanceValidator b = new BalanceValidator();
        LimitValidator l = new LimitValidator(10000.0);
        b.setNext(l);
        return b.handle(customer, amount);
    }

    protected boolean validateForTransfer(Customer origin, BigDecimal amount) {
        BalanceValidator b = new BalanceValidator();
        LimitValidator l = new LimitValidator(10000.0);
        b.setNext(l);
        return b.handle(origin, amount);
    }

    protected boolean validateForDeposit(BigDecimal amount) {
        LimitValidator l = new LimitValidator(50000.0);
        // deposit validator doesn't need customer, but we adapt by creating a dummy check:
        return l.handle(null, amount);
    }

    // Facade methods accept BigDecimal amounts (overload with double convenience)
    public boolean deposit(String email, BigDecimal amount) {
        if (amount == null) return false;
        if (!validateForDeposit(amount)) {
            System.out.println("Depósito rechazado por validaciones");
            return false;
        }
        Customer c = bankCore.findCustomerByEmail(email);
        if (c == null) {
            System.out.println("Cliente no encontrado");
            return false;
        }
        return c.getAccount().deposit(amount);
    }

    public boolean deposit(String email, double amount) {
        return deposit(email, BigDecimal.valueOf(amount));
    }

    public boolean withdraw(String email, BigDecimal amount) {
        Customer c = bankCore.findCustomerByEmail(email);
        if (c == null) {
            System.out.println("Cliente no encontrado");
            return false;
        }
        if (!validateForWithdraw(c, amount)) {
            System.out.println("Retiro rechazado por validaciones");
            return false;
        }
        return c.getAccount().withdraw(amount);
    }

    public boolean withdraw(String email, double amount) {
        return withdraw(email, BigDecimal.valueOf(amount));
    }

    public boolean transfer(String originEmail, String destEmail, BigDecimal amount) {
        Customer origin = bankCore.findCustomerByEmail(originEmail);
        Customer dest = bankCore.findCustomerByEmail(destEmail);
        if (origin == null || dest == null) {
            System.out.println("Uno de los clientes no existe");
            return false;
        }
        if (origin == dest) {
            System.out.println("No puede transferirse a sí mismo");
            return false;
        }
        if (!validateForTransfer(origin, amount)) {
            System.out.println("Transferencia rechazada por validaciones");
            return false;
        }
        return origin.getAccount().transfer(amount, dest.getAccount());
    }

    public boolean transfer(String originEmail, String destEmail, double amount) {
        return transfer(originEmail, destEmail, BigDecimal.valueOf(amount));
    }

    public boolean requestLoan(String email, BigDecimal amount) {
        Customer c = bankCore.findCustomerByEmail(email);
        if (c == null) {
            System.out.println("Cliente no encontrado");
            return false;
        }
        return c.getLoan().approveLoan(amount);
    }

    public boolean requestLoan(String email, double amount) {
        return requestLoan(email, BigDecimal.valueOf(amount));
    }

    public void applyInterest(String email) {
        Customer c = bankCore.findCustomerByEmail(email);
        if (c != null) c.getAccount().applyInterest();
        else System.out.println("Cliente no encontrado");
    }

    public void showAccount(String email) {
        Customer c = bankCore.findCustomerByEmail(email);
        if (c == null) {
            System.out.println("Cliente no encontrado");
            return;
        }
        System.out.println("Saldo actual: $" + c.getAccount().getBalance());
        System.out.println("Transacciones:");
        for (Transaction t : c.getAccount().getTransactions()) {
            System.out.println(" - " + t);
        }
    }

    public void showCustomers() {
        bankCore.listCustomers();
    }
}
