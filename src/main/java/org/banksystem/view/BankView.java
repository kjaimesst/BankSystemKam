package org.banksystem.view;

import org.banksystem.controller.*;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Vista por consola (MVC - View). Interactúa con el usuario.
 */
public class BankView {
    private final BankFacade facade;
    private final TransactionManager manager;
    private final Scanner sc;

    public BankView(BankFacade facade) {
        this.facade = facade;
        this.manager = new TransactionManager();
        this.sc = new Scanner(System.in);
    }

    public void start() {
        System.out.println("KamBank");
        boolean run = true;
        while (run) {
            try {
                System.out.println("\n1. Registrar cliente");
                System.out.println("2. Depositar");
                System.out.println("3. Retirar");
                System.out.println("4. Transferir");
                System.out.println("5. Solicitar préstamo");
                System.out.println("6. Ver cuenta");
                System.out.println("7. Listar clientes");
                System.out.println("8. Historial de comandos");
                System.out.println("0. Salir");
                System.out.print("Opción: ");
                int opt = sc.nextInt();
                sc.nextLine();

                switch (opt) {
                    case 1 -> register();
                    case 2 -> deposit();
                    case 3 -> withdraw();
                    case 4 -> transfer();
                    case 5 -> requestLoan();
                    case 6 -> showAccount();
                    case 7 -> facade.showCustomers();
                    case 8 -> manager.showHistory();
                    case 0 -> {
                        run = false;
                        System.out.println("Saliendo... gracias");
                    }
                    default -> System.out.println("Opción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida");
                sc.nextLine();
            }
        }
        sc.close();
    }

    private void register() {
        System.out.print("Nombre: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Tipo de cuenta (ahorros/corriente): ");
        String type = sc.nextLine();
        facade.registerCustomer(name, email, type);
    }

    private void deposit() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Monto: ");
        double amount = sc.nextDouble(); sc.nextLine();
        manager.executeCommand(new DepositCommand(facade, email, BigDecimal.valueOf(amount)));
    }

    private void withdraw() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Monto: ");
        double amount = sc.nextDouble(); sc.nextLine();
        manager.executeCommand(new WithdrawCommand(facade, email, BigDecimal.valueOf(amount)));
    }

    private void transfer() {
        System.out.print("Email origen: ");
        String from = sc.nextLine();
        System.out.print("Email destino: ");
        String to = sc.nextLine();
        System.out.print("Monto: ");
        double amount = sc.nextDouble(); sc.nextLine();
        manager.executeCommand(new TransferCommand(facade, from, to, BigDecimal.valueOf(amount)));
    }

    private void requestLoan() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Monto préstamo: ");
        double amount = sc.nextDouble(); sc.nextLine();
        facade.requestLoan(email, amount);
    }

    private void showAccount() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        facade.showAccount(email);
    }
}
