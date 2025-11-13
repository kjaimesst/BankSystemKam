package org.banksystem;

import org.banksystem.controller.BankFacade;
import org.banksystem.view.BankView;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // un solo scanner global
        BankFacade facade = new BankFacade();
        BankView view = new BankView(facade, scanner); // se pasa al constructor

        System.out.print("¿Ingresar como administrador? (si (s) - no (n) ): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        boolean isAdmin = answer.equals("s") || answer.equals("si");
        view.start(isAdmin);
    }
}

