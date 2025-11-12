package org.banksystem.junit;

import org.banksystem.model.Account;
import org.banksystem.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account a1;
    private Account a2;
    private TestNotification n1;
    private TestNotification n2;

    @BeforeEach
    void setUp() {
        a1 = new TestAccount();
        a2 = new TestAccount();
        n1 = new TestNotification();
        n2 = new TestNotification();
        a1.addObserver(n1);
        a2.addObserver(n2);
    }

    @Test
    void deposit_success_updatesBalanceTransactionsAndNotifies() {
        boolean ok = a1.deposit(new BigDecimal("1000"));
        assertTrue(ok, "El depósito debería retornar true");
        assertEquals(0, a1.getBalance().compareTo(new BigDecimal("1000")), "Balance debe ser 1000");
        assertEquals(1, a1.getTransactions().size(), "Debe agregarse una transacción por el depósito");
        assertNotNull(n1.lastMessage, "Debe haberse notificado al observer");
        assertTrue(n1.lastMessage.startsWith("Depósito realizado por "), "Mensaje de notificación esperado");
    }

    @Test
    void deposit_invalid_amount_notAccepted_andNotifies() {
        boolean ok = a1.deposit(BigDecimal.ZERO);
        assertFalse(ok, "Depósito de 0 debe fallar");
        assertEquals("Intento de depósito inválido: el monto debe ser mayor que 0", n1.lastMessage);
        // otra prueba: null
        ok = a1.deposit(null);
        assertFalse(ok, "Depósito null debe fallar");
        assertEquals("Intento de depósito inválido: el monto debe ser mayor que 0", n1.lastMessage);
    }

    @Test
    void withdraw_success_and_insufficientFunds() {
        a1.deposit(new BigDecimal("800"));
        boolean ok = a1.withdraw(new BigDecimal("300"));
        assertTrue(ok, "Retiro válido debe retornar true");
        assertEquals(0, a1.getBalance().compareTo(new BigDecimal("500")), "Balance debe ser 500 después del retiro");
        assertEquals(2, a1.getTransactions().size(), "Debe haber 2 transacciones (depósito + retiro)");
        assertTrue(n1.lastMessage.startsWith("Retiro realizado por "), "Notificación de retiro esperada");

        // Intento de retirar más de lo disponible
        boolean ok2 = a1.withdraw(new BigDecimal("1000"));
        assertFalse(ok2, "Retiro mayor al balance debe fallar");
        assertEquals("Retiro fallido: fondos insuficientes", n1.lastMessage);
    }

    @Test
    void transfer_betweenAccounts_updatesBothAndNotifiesBoth() {
        a1.deposit(new BigDecimal("1200"));
        boolean ok = a1.transfer(new BigDecimal("450"), a2);
        assertTrue(ok, "Transferencia válida debe retornar true");
        assertEquals(0, a1.getBalance().compareTo(new BigDecimal("750")), "Balance origen = 1200 - 450 = 750");
        assertEquals(0, a2.getBalance().compareTo(new BigDecimal("450")), "Balance destino = 450");
        assertEquals(2, a1.getTransactions().size(), "Origen: depósito + transferencia enviada");
        assertEquals(1, a2.getTransactions().size(), "Destino: transferencia recibida");
        assertTrue(n1.lastMessage.startsWith("Transferencia enviada por "), "Mensaje en cuenta origen");
        assertTrue(n2.lastMessage.startsWith("Ha recibido una transferencia de "), "Mensaje en cuenta destino");
    }

    @Test
    void transfer_invalidDestinationAndAmount() {
        // destino null
        boolean ok = a1.transfer(new BigDecimal("100"), null);
        assertFalse(ok);
        assertEquals("Transferencia fallida: cuenta destino inválida", n1.lastMessage);

        // monto inválido
        ok = a1.transfer(BigDecimal.ZERO, a2);
        assertFalse(ok);
        assertEquals("Transferencia fallida: monto inválido", n1.lastMessage);
    }

    @Test
    void applyInterest_addsInterest_whenStrategySet() {
        a1.deposit(new BigDecimal("1000"));
        // estrategia del 10%
        a1.setInterestStrategy(balance -> balance.multiply(new BigDecimal("0.10")));
        a1.applyInterest();
        // 1000 + 100 = 1100
        assertEquals(0, a1.getBalance().compareTo(new BigDecimal("1100")), "Balance debe incluir el interés aplicado");
        assertTrue(n1.lastMessage.startsWith("Interés aplicado: "), "Debe notificarse el interés aplicado");
        assertEquals(2, a1.getTransactions().size(), "Depósito + Interés");
    }

    @Test
    void applyInterest_noStrategy_doesNothing() {
        a1.deposit(new BigDecimal("500"));
        a1.applyInterest(); // no strategy -> no cambio
        assertEquals(0, a1.getBalance().compareTo(new BigDecimal("500")));
    }

    @Test
    void getTransactions_isUnmodifiable() {
        a1.deposit(new BigDecimal("100"));
        assertThrows(UnsupportedOperationException.class, () -> a1.getTransactions().add(null),
                "La lista devuelta debe ser inmodificable");
    }


    static class TestAccount extends Account {
        @Override
        public String getType() {
            return "TEST";
        }
    }


    static class TestNotification implements Notification {
        String lastMessage = null;
        int updateCount = 0;

        @Override
        public void update(String message) {
            this.lastMessage = message;
            this.updateCount++;
        }
    }

}