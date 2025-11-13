package org.banksystem.model;

public class Customer {
    private final String name;
    private final String email;
    private final Account account;
    private final Loan loan;

    public Customer(String name, String email, Account account) {
        this.name = name;
        this.email = email;
        this.account = account;
        this.account.addObserver(new ConsoleNotification(name)); // suscribir notificaciones
        this.loan = new Loan(this);
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Account getAccount() {
        return account;
    }
    public Loan getLoan() {
        return loan;
    }
}

