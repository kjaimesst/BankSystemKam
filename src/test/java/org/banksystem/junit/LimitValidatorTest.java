package org.banksystem.junit;

import org.banksystem.controller.LimitValidator;
import org.banksystem.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class LimitValidatorTest {

    private LimitValidator validator;
    private Account mockAccount;

    @BeforeEach
    void setUp() {
        validator = new LimitValidator();

        mockAccount = Mockito.mock(Account.class);
    }

    @Test
    void testHandle_WhenAmountIsBelowLimit_ShouldReturnTrue() {
        BigDecimal amount = new BigDecimal("5000000");

        boolean result = validator.handle(mockAccount, amount);

        assertTrue(result, "Debe permitir montos menores al límite");
    }

    @Test
    void testHandle_WhenAmountExceedsLimit_ShouldReturnFalse() {
        BigDecimal amount = new BigDecimal("15000000");

        boolean result = validator.handle(mockAccount, amount);

        assertFalse(result, "Debe rechazar montos mayores al límite");
    }

    @Test
    void testHandle_WhenExactlyAtLimit_ShouldReturnTrue() {
        BigDecimal amount = new BigDecimal("10000000");

        boolean result = validator.handle(mockAccount, amount);

        assertTrue(result, "Debe permitir montos iguales al límite");
    }
}