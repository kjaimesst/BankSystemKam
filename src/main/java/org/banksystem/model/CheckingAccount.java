package org.banksystem.model;

/**
 * checkingAccount + concrete product of fctorymethod
 */
public class CheckingAccount extends Account {
    @Override
    public String getType() {
        return "Corriente";
    }
}
