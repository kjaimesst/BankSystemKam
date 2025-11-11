package org.banksystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Account.
 */
public class AccountTest {
    private Account account;

    @BeforeEach
    void setup() {
        account = new SavingsAccount();
    }

    @Test
    void testDepositIncreasesBalance() {
        account.deposit(new BigDecimal("1000000"));
        assertEquals(new BigDecimal("1000000"), account.getBalance());
    }

    @Test
    void testWithdrawDecreasesBalance() {
        account.deposit(new BigDecimal("2000000"));
        account.withdraw(new BigDecimal("500000"));
        assertEquals(new BigDecimal("1500000"), account.getBalance());
    }

    @Test
    void testWithdrawFailsIfInsufficientFunds() {
        boolean result = account.withdraw(new BigDecimal("10000"));
        assertFalse(result);
    }
}
