package org.banksystem.e2e;
import org.banksystem.controller.BankFacade;
import org.banksystem.model.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba E2E (de extremo a extremo) del sistema bancario completo.
 * Flujo: Registro → Depósito → Retiro → Transferencia
 */
class BankSystemE2ETest {

    private BankFacade facade;
    private BankCoreSingleton core;

    private Customer ori;
    private Customer kami;

    @BeforeEach
    void setUp() {
        facade = new BankFacade();
        core = BankCoreSingleton.getInstance();
        core.clearForTests();

        // Registro de usuarios
        ori = facade.registerCustomer("Oriana", "ori@bank.com", "Ahorros");
        kami = facade.registerCustomer("Kamila", "kam@bank.com", "Ahorros");

        assertNotNull(ori);
        assertNotNull(kami);
    }

    @Test
    void testFullUserFlow_EndToEnd() {
        Account accOri = ori.getAccount();
        Account accKami = kami.getAccount();

        // --- Paso 1: depósito ---
        facade.depositMoney(accOri, new BigDecimal("5000"));
        assertEquals(new BigDecimal("5000"), accOri.getBalance(), "Saldo después del depósito debe ser 5000");

        // --- Paso 2: retiro ---
        facade.withdrawMoney(accOri, new BigDecimal("2000"));
        assertEquals(new BigDecimal("3000"), accOri.getBalance(), "Saldo después del retiro debe ser 3000");

        // --- Paso 3: transferencia ---
        facade.transferMoney(accOri, accKami, new BigDecimal("1000"));

        // --- Validaciones finales ---
        assertEquals(new BigDecimal("2000"), accOri.getBalance(), "Saldo final de Oriana debe ser 2000");
        assertEquals(new BigDecimal("1000"), accKami.getBalance(), "Saldo final de Jhair debe ser 1000");

        // --- Verificar transacciones registradas ---
        assertEquals(3, accOri.getTransactions().size(), "Oriana debe tener 3 transacciones (depósito, retiro, transferencia)");
        assertEquals(1, accKami.getTransactions().size(), "Jhair debe tener 1 transacción (transferencia recibida)");
    }
}