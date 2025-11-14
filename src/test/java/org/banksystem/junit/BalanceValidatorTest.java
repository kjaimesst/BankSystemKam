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

        when(mockAccount.getBalance()).thenReturn(new BigDecimal("1000"));

        boolean result = validator.handle(mockAccount, new BigDecimal("-500"));

        System.out.println("RESULTADO DEL VALIDATOR = " + result);

        assertTrue(result, "Debe permitir si hay fondos suficientes");

    }

    @Test
    void testHandle_WhenBalanceIsInsufficient_ShouldReturnFalse() {

        when(mockAccount.getBalance()).thenReturn(new BigDecimal("200"));

        boolean result = validator.handle(mockAccount, new BigDecimal("-500"));

        assertFalse(result, "Debe rechazar si no hay fondos suficientes");
        verify(mockAccount).notifyAllObservers(contains("fondos insuficientes"));
    }

    @Test
    void testHandle_WhenNextHandlerExists_ShouldDelegate() {

        when(mockAccount.getBalance()).thenReturn(new BigDecimal("1000"));


        TransactionHandler next = Mockito.mock(TransactionHandler.class);
        validator.setNextHandler(next);
        when(next.handle(mockAccount, new BigDecimal("-500"))).thenReturn(true);

        boolean result = validator.handle(mockAccount, new BigDecimal("-500"));

        assertTrue(result, "Debe delegar al siguiente validador");
        verify(next).handle(mockAccount, new BigDecimal("-500"));
    }
}