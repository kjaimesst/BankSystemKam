package org.banksystem.controller;

/**
 * Proxy para controlar acceso a ciertas operaciones
 */
public class BankProxy extends BankFacade {
    private final boolean isAdmin;

    public BankProxy(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean withdraw(String email, double amount) {
        if (!isAdmin) {
            System.out.println("Acceso denegado: operación restringida para usuarios no administradores.");
            return false;
        }
        return super.withdraw(email, amount);
    }

    @Override
    public boolean requestLoan(String email, double amount) {
        if (!isAdmin) {
            System.out.println("Acceso denegado: solo administradores pueden aprobar préstamos directamente.");
            return false;
        }
        return super.requestLoan(email, amount);
    }
}
