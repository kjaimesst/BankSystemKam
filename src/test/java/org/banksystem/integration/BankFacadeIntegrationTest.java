package org.banksystem.integration;


import org.banksystem.controller.BankFacade;
import org.banksystem.model.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankFacadeIntegrationTest {

    private BankFacade facade;
    private Customer ori;
    private Customer mela;

    @BeforeEach
    void setUp() {
        facade = new BankFacade();
        BankCoreSingleton.getInstance().clearForTests();

        ori = facade.registerCustomer("Oriana", "ori@bank.com", "Ahorros");
        mela = facade.registerCustomer("Mela", "mela@bank.com", "Ahorros");
    }

    @Test
    void testDepositMoney_ShouldIncreaseBalance() {
        Account acc = ori.getAccount();
        BigDecimal initial = acc.getBalance();

        facade.depositMoney(acc, new BigDecimal("2000"));

        assertTrue(acc.getBalance().compareTo(initial) > 0, "El saldo debe aumentar después del depósito");
    }

    @Test
    void testWithdrawMoney_ShouldDecreaseBalance() {
        Account acc = ori.getAccount();
        acc.deposit(new BigDecimal("3000"));

        facade.withdrawMoney(acc, new BigDecimal("1000"));

        assertEquals(new BigDecimal("2000"), acc.getBalance(), "El saldo debe reducirse tras el retiro");
    }

    @Test
    void testTransferMoney_ShouldMoveFundsBetweenAccounts() {
        Account from = ori.getAccount();
        Account to = mela.getAccount();
        from.deposit(new BigDecimal("5000"));

        facade.transferMoney(from, to, new BigDecimal("2000"));

        assertEquals(new BigDecimal("3000"), from.getBalance(), "Debe restar al origen");
        assertEquals(new BigDecimal("2000"), to.getBalance(), "Debe sumar al destino");
    }
}