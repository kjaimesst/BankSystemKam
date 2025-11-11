package org.banksystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Núcleo del banco: Singleton que guarda clientes.
 */
public class BankCore {
    private static volatile BankCore instance;
    private final List<Customer> customers;


    private BankCore() {
        customers = new ArrayList<>();
    }

    public static BankCore getInstance() {
        if (instance == null) {
            synchronized (BankCore.class) {
                if (instance == null) instance = new BankCore();
            }
        }
        return instance;
    }

    public synchronized Customer registerCustomer(String name, String email, String accountType) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: correo inválido.");
            return null;
        }
        String norm = email.trim().toLowerCase();
        Customer existing = findCustomerByEmail(norm);
        if (existing != null) {
            System.out.println("Ya existe un cliente con ese correo: " + existing.getName());
            return existing;
        }
        Account acc = AccountFactory.createAccount(accountType);
        Customer c = new Customer(name, norm, acc);
        customers.add(c);
        System.out.println("Cliente registrado: " + name + " (" + norm + ")");
        return c;
    }

    public synchronized Customer findCustomerByEmail(String email) {
        if (email == null) return null;
        String norm = email.trim().toLowerCase();
        for (Customer c : customers) {
            if (c.getEmail().equalsIgnoreCase(norm)) return c;
        }
        return null;
    }

    public synchronized List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public synchronized void listCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("Clientes registrados:");
        for (Customer c : customers) {
            System.out.println("- " + c.getName() + " (" + c.getEmail() + ") - Cuenta: " + c.getAccount().getType());
        }
    }

    public synchronized void clearForTests() {
        customers.clear();
    }
}
