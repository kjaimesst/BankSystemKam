package org.banksystem.model;

/**
 * Display name: CheckingAccount — (Concrete Product of FactoryMethod)
 */
public class CheckingAccount extends Account {
    @Override
    public String getType() {
        return "Corriente";
    }
}
