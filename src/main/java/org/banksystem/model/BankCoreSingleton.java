package org.banksystem.model;

import java.util.ArrayList;
import java.util.List;

public class BankCoreSingleton {
    private static volatile BankCoreSingleton instance;
    private final List<Customer> customers;

    private BankCoreSingleton() {
        this.customers = new ArrayList<>();
    }

    public static BankCoreSingleton getInstance() {
        if (instance == null) {
            synchronized (BankCoreSingleton.class) {
                if (instance == null) instance = new BankCoreSingleton();
            }
        }
        return instance;
    }

    public synchronized Customer registerCustomer(String name, String email, String accountType) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Error: correo inválido");
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

    //  limpiar estado entre pruebas

    public synchronized void clearForTests() {
        customers.clear();
    }
}
