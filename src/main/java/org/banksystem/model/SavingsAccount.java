package org.banksystem.model;

/**
 * concrete product of factoryMethod)
 */

public class SavingsAccount extends Account {
    @Override
    public String getType() {
        return "Ahorros";
    }
}
