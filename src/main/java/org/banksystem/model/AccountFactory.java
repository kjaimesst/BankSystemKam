package org.banksystem.model;

/**
 * accountFactory - factoryMethod
 * fábrica simple para crear cuentas por tipo
 */
public class AccountFactory {
    public static Account createAccount(String type) {
        if (type == null) return new SavingsAccount();
        switch (type.trim().toLowerCase()) {
            case "corriente":
            case "checking":
                return new CheckingAccount();
            case "ahorros":
            case "savings":
            default:
                return new SavingsAccount();
        }
    }
}
