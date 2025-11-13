package org.banksystem.controller;

import org.banksystem.model.Customer;
import org.banksystem.model.Account;
import java.math.BigDecimal;

/**
 * patron proxy: controla el acceso a las operaciones del banco segun el rol
 */
public class BankProxy {
    private final BankFacade facade;
    private final boolean isAdmin;

    public BankProxy(BankFacade facade, boolean isAdmin) {
        this.facade = facade;
        this.isAdmin = isAdmin;
    }

    public void performDeposit(Account account, BigDecimal amount) {
        if (!isAdmin) {
            System.out.println("Acceso denegado: solo administradores pueden realizar depósitos globales.");
            return;
        }
        facade.depositMoney(account, amount);
    }

    public void performTransfer(Account from, Account to, BigDecimal amount) {
        facade.transferMoney(from, to, amount);
    }

    public void showAllCustomers() {
        if (!isAdmin) {
            System.out.println("Acceso restringido: solo administradores pueden listar todos los clientes.");
            return;
        }
        facade.listCustomers();
    }

    public Customer registerCustomer(String name, String email, String accountType) {
        if (!isAdmin) {
            System.out.println("Solo administradores pueden registrar nuevos clientes.");
            return null;
        }
        return facade.registerCustomer(name, email, accountType);
    }
}