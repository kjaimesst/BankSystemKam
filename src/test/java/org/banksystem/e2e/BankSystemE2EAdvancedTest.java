package org.banksystem.e2e;

import org.banksystem.controller.BankFacade;
import org.banksystem.model.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BankSystemE2EAdvancedTest {

    private BankFacade facade;
    private BankCoreSingleton core;

    @BeforeEach
    void setUp() {
        facade = new BankFacade();
        core = BankCoreSingleton.getInstance();
        core.clearForTests();
    }

    @Test
    @DisplayName("E2E: Flujo con fondos insuficientes debe rechazar operación")
    void testInsufficientFunds_ShouldRejectTransaction() {
        Customer customer = facade.registerCustomer("Ana", "ana@bank.com", "Ahorros");
        Account account = customer.getAccount();

        BigDecimal initialBalance = account.getBalance();
        facade.withdrawMoney(account, new BigDecimal("1000"));

        assertEquals(initialBalance, account.getBalance(), 
            "El saldo no debe cambiar cuando no hay fondos suficientes");
    }

    @Test
    @DisplayName("E2E: Múltiples clientes con transacciones concurrentes")
    void testMultipleCustomers_WithConcurrentTransactions() {
        // Registrar 3 clientes
        Customer alice = facade.registerCustomer("Alice", "alice@bank.com", "Ahorros");
        Customer bob = facade.registerCustomer("Bob", "bob@bank.com", "Corriente");
        Customer charlie = facade.registerCustomer("Charlie", "charlie@bank.com", "Ahorros");

        assertNotNull(alice);
        assertNotNull(bob);
        assertNotNull(charlie);
        assertEquals(3, core.getCustomers().size());

        Account accAlice = alice.getAccount();
        Account accBob = bob.getAccount();
        Account accCharlie = charlie.getAccount();

        facade.depositMoney(accAlice, new BigDecimal("10000"));
        facade.transferMoney(accAlice, accBob, new BigDecimal("3000"));

        facade.transferMoney(accBob, accCharlie, new BigDecimal("1000"));

        assertEquals(new BigDecimal("7000"), accAlice.getBalance());
        assertEquals(new BigDecimal("2000"), accBob.getBalance());
        assertEquals(new BigDecimal("1000"), accCharlie.getBalance());

        assertTrue(accAlice.getTransactions().size() >= 2, "Alice debe tener al menos 2 transacciones");
        assertTrue(accBob.getTransactions().size() >= 2, "Bob debe tener al menos 2 transacciones");
        assertTrue(accCharlie.getTransactions().size() >= 1, "Charlie debe tener al menos 1 transacción");
    }

    @Test
    @DisplayName("E2E: Diferentes tipos de cuenta (Ahorros vs Corriente)")
    void testDifferentAccountTypes() {
        Customer savings = facade.registerCustomer("Savings User", "savings@bank.com", "Ahorros");
        Customer checking = facade.registerCustomer("Checking User", "checking@bank.com", "Corriente");

        assertNotNull(savings);
        assertNotNull(checking);

        Account savingsAcc = savings.getAccount();
        Account checkingAcc = checking.getAccount();

        assertInstanceOf(SavingsAccount.class, savingsAcc, "Debe ser cuenta de ahorros");
        assertInstanceOf(CheckingAccount.class, checkingAcc, "Debe ser cuenta corriente");

        facade.depositMoney(savingsAcc, new BigDecimal("5000"));
        facade.depositMoney(checkingAcc, new BigDecimal("3000"));

        assertEquals(new BigDecimal("5000"), savingsAcc.getBalance());
        assertEquals(new BigDecimal("3000"), checkingAcc.getBalance());
    }

    @Test
    @DisplayName("E2E: Intento de registro con correo duplicado")
    void testDuplicateEmailRegistration() {
        Customer first = facade.registerCustomer("First User", "same@bank.com", "Ahorros");
        Customer second = facade.registerCustomer("Second User", "same@bank.com", "Corriente");

        assertNotNull(first);
        assertNotNull(second);
        assertSame(first, second, "Debe retornar el mismo cliente para email duplicado");
        assertEquals(1, core.getCustomers().size(), "Solo debe haber un cliente registrado");
    }

    @Test
    @DisplayName("E2E: Serie de operaciones con validación de historial")
    void testCompleteTransactionHistory() {
        Customer customer = facade.registerCustomer("Historial User", "historial@bank.com", "Ahorros");
        Account account = customer.getAccount();

        facade.depositMoney(account, new BigDecimal("1000"));
        facade.depositMoney(account, new BigDecimal("500"));
        facade.withdrawMoney(account, new BigDecimal("300"));
        facade.depositMoney(account, new BigDecimal("200"));

        assertEquals(new BigDecimal("1400"), account.getBalance());

        List<Transaction> transactions = account.getTransactions();
        assertTrue(transactions.size() >= 4, "Debe haber al menos 4 transacciones registradas");

         long deposits = transactions.stream()
            .filter(t -> t.getType().equalsIgnoreCase("Depósito") || t.getType().equalsIgnoreCase("DEPOSIT"))
            .count();
        long withdrawals = transactions.stream()
            .filter(t -> t.getType().equalsIgnoreCase("Retiro") || t.getType().equalsIgnoreCase("WITHDRAWAL"))
            .count();

        assertTrue(deposits >= 3, "Debe haber al menos 3 depósitos");
        assertTrue(withdrawals >= 1, "Debe haber al menos 1 retiro");
    }

    @Test
    @DisplayName("E2E: Transferencia circular entre tres cuentas")
    void testCircularTransfer() {
        Customer c1 = facade.registerCustomer("User 1", "user1@bank.com", "Ahorros");
        Customer c2 = facade.registerCustomer("User 2", "user2@bank.com", "Ahorros");
        Customer c3 = facade.registerCustomer("User 3", "user3@bank.com", "Ahorros");

        Account acc1 = c1.getAccount();
        Account acc2 = c2.getAccount();
        Account acc3 = c3.getAccount();

        facade.depositMoney(acc1, new BigDecimal("3000"));
        facade.depositMoney(acc2, new BigDecimal("2000"));
        facade.depositMoney(acc3, new BigDecimal("1000"));

        facade.transferMoney(acc1, acc2, new BigDecimal("500"));
        facade.transferMoney(acc2, acc3, new BigDecimal("500"));
        facade.transferMoney(acc3, acc1, new BigDecimal("500"));

        assertEquals(new BigDecimal("3000"), acc1.getBalance());
        assertEquals(new BigDecimal("2000"), acc2.getBalance());
        assertEquals(new BigDecimal("1000"), acc3.getBalance());
    }

    @Test
    @DisplayName("E2E: Validación de correo inválido")
    void testInvalidEmailRegistration() {
        Customer customer1 = facade.registerCustomer("Invalid User", "", "Ahorros");
        Customer customer2 = facade.registerCustomer("Null Email User", null, "Ahorros");

        assertNull(customer1, "No debe registrar con correo vacío");
        assertNull(customer2, "No debe registrar con correo nulo");
        assertEquals(0, core.getCustomers().size(), "No debe haber clientes registrados");
    }
}
