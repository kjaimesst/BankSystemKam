package org.banksystem.model;

/**
 * Display name: SavingsAccount — (Concrete Product of FactoryMethod)
 */
public class SavingsAccount extends Account {
    @Override
    public String getType() {
        return "Ahorros";
    }
}
