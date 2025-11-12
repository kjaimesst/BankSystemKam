package org.banksystem.junit;

import org.banksystem.controller.BalanceValidator;
import org.banksystem.controller.TransactionHandler;
import org.banksystem.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceValidatorTest {

    private BalanceValidator validator;
    private Account mockAccount;

    @BeforeEach
    void setUp() {
        validator = new BalanceValidator();
        mockAccount = Mockito.mock(Account.class);
    }

    @Test
    void testHandle_WhenBalanceIsSufficient_ShouldReturnTrue() {
        // balance = 1000, amount = 500 → válido
        when(mockAccount.getBalance()).thenReturn(new BigDecimal("1000"));

        boolean result = validator.handle(mockAccount, new BigDecimal("500"));

        assertTrue(result, "Debe aceptar la operación si hay fondos suficientes");
        verify(mockAccount, never()).notifyAllObservers(anyString());
    }

    @Test
    void testHandle_WhenBalanceIsInsufficient_ShouldReturnFalse() {
        // balance = 200, amount = 500 → insuficiente
        when(mockAccount.getBalance()).thenReturn(new BigDecimal("200"));

        boolean result = validator.handle(mockAccount, new BigDecimal("500"));

        assertFalse(result, "Debe rechazar si no hay fondos suficientes");
        verify(mockAccount).notifyAllObservers(contains("fondos insuficientes"));
    }

    @Test
    void testHandle_WhenNextHandlerExists_ShouldDelegate() {
        // balance = 1000 → suficiente
        when(mockAccount.getBalance()).thenReturn(new BigDecimal("1000"));

        // Simulamos el siguiente validador
        TransactionHandler next = Mockito.mock(TransactionHandler.class);
        validator.setNextHandler(next);
        when(next.handle(mockAccount, new BigDecimal("1000"))).thenReturn(true);

        boolean result = validator.handle(mockAccount, new BigDecimal("1000"));

        assertTrue(result, "Debe delegar al siguiente validador");
        verify(next).handle(mockAccount, new BigDecimal("1000"));
    }
}