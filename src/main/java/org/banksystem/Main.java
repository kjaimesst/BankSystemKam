package org.banksystem;

import org.banksystem.controller.BankFacade;
import org.banksystem.view.BankView;


public class Main {
    public static void main(String[] args) {
        BankFacade facade = new BankFacade();
        BankView view = new BankView(facade);

        System.out.println("¿Ingresar como administrador? (s/n): ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String answer = scanner.nextLine().trim().toLowerCase();

        boolean isAdmin = answer.equals("s") || answer.equals("si");
        view.start(isAdmin);
    }
}
