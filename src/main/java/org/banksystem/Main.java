package org.banksystem;

import org.banksystem.controller.BankFacade;
import org.banksystem.view.BankView;


public class Main {
    public static void main(String[] args) {
        BankFacade facade = new BankFacade(); // o new BankProxy(true) para admin
        BankView view = new BankView(facade);
        view.start();
    }
}
