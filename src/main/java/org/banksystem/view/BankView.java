package org.banksystem.view;

import org.banksystem.controller.*;
import org.banksystem.model.*;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Display name: BankView — MVC View
 * Vista de consola que permite interactuar con el sistema bancario.
 */
public class BankView {
    private final BankFacade facade;
    private final Scanner scanner;

    public BankView(BankFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void start(boolean isAdmin) {
        BankProxy proxy = new BankProxy(facade, isAdmin);
        System.out.println("\n=== BIENVENIDO AL SISTEMA BANCARIO ===");
        if (isAdmin) {
            adminMenu(proxy);
        } else {
            customerMenu(proxy);
        }
    }

    private void adminMenu(BankProxy proxy) {
        while (true) {
            System.out.println("\n--- MENÚ ADMINISTRADOR ---");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Depositar dinero");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.print("Nombre del cliente: ");
                    String name = scanner.nextLine();
                    System.out.print("Correo electrónico: ");
                    String email = scanner.nextLine();
                    System.out.print("Tipo de cuenta (ahorros/corriente): ");
                    String type = scanner.nextLine();
                    proxy.registerCustomer(name, email, type);
                    break;
                case "2":
                    proxy.showAllCustomers();
                    break;
                case "3":
                    System.out.print("Correo del cliente: ");
                    String emailDep = scanner.nextLine();
                    Customer c = facade.registerCustomer("", emailDep, "");
                    if (c == null) {
                        System.out.println("Cliente no encontrado.");
                        break;
                    }
                    System.out.print("Monto a depositar: ");
                    BigDecimal amountDep = new BigDecimal(scanner.nextLine());
                    proxy.performDeposit(c.getAccount(), amountDep);
                    break;
                case "4":
                    System.out.println("Saliendo del sistema administrador...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void customerMenu(BankProxy proxy) {
        System.out.print("Ingrese su correo registrado: ");
        String email = scanner.nextLine();
        Customer c = BankCoreSingleton.getInstance().findCustomerByEmail(email);
        if (c == null) {
            System.out.println("Cliente no encontrado. Contacte a un administrador.");
            return;
        }

        while (true) {
            System.out.println("\n--- MENÚ CLIENTE ---");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Transferir a otro cliente");
            System.out.println("5. Solicitar préstamo");
            System.out.println("6. Pagar préstamo");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Saldo actual: " + c.getAccount().getBalance());
                    break;
                case "2":
                    System.out.print("Monto a depositar: ");
                    BigDecimal dep = new BigDecimal(scanner.nextLine());
                    proxy.performDeposit(c.getAccount(), dep);
                    break;
                case "3":
                    System.out.print("Monto a retirar: ");
                    BigDecimal wd = new BigDecimal(scanner.nextLine());
                    c.getAccount().withdraw(wd);
                    break;
                case "4":
                    System.out.print("Correo del destinatario: ");
                    String emailDest = scanner.nextLine();
                    Customer dest = BankCoreSingleton.getInstance().findCustomerByEmail(emailDest);
                    if (dest == null) {
                        System.out.println("Destinatario no encontrado.");
                        break;
                    }
                    System.out.print("Monto a transferir: ");
                    BigDecimal trans = new BigDecimal(scanner.nextLine());
                    proxy.performTransfer(c.getAccount(), dest.getAccount(), trans);
                    break;
                case "5":
                    System.out.print("Monto del préstamo: ");
                    BigDecimal loan = new BigDecimal(scanner.nextLine());
                    c.getLoan().approveLoan(loan);
                    break;
                case "6":
                    System.out.print("Monto a pagar: ");
                    BigDecimal pay = new BigDecimal(scanner.nextLine());
                    c.getLoan().makePayment(pay);
                    break;
                case "7":
                    System.out.println("Cerrando sesión...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
}
