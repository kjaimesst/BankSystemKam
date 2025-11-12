package org.banksystem.integration;
import org.banksystem.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BankCoreSingletonIntegrationTest {

    private BankCoreSingleton core;

    @BeforeEach
    void setUp() {
        core = BankCoreSingleton.getInstance();
        core.clearForTests(); // limpiar lista de clientes entre pruebas
    }

    @Test
    void testRegisterCustomer_NewCustomer_ShouldBeAdded() {
        Customer c = core.registerCustomer("Oriana", "ori@bank.com", "Ahorros");

        assertNotNull(c);
        assertEquals("Oriana", c.getName());
        assertEquals("ori@bank.com", c.getEmail());
        assertNotNull(c.getAccount());
        assertEquals(1, core.getCustomers().size());
    }

    @Test
    void testRegisterCustomer_DuplicateEmail_ShouldReturnExisting() {
        Customer first = core.registerCustomer("Oriana", "ori@bank.com", "Ahorros");
        Customer duplicate = core.registerCustomer("Otra Ori", "ori@bank.com", "Corriente");

        assertSame(first, duplicate, "Debe devolver el cliente existente");
        assertEquals(1, core.getCustomers().size(), "No debe crear duplicados");
    }

    @Test
    void testFindCustomerByEmail_ShouldReturnCorrectCustomer() {
        core.registerCustomer("Oriana", "ori@bank.com", "Ahorros");
        Customer found = core.findCustomerByEmail("ori@bank.com");

        assertNotNull(found);
        assertEquals("Oriana", found.getName());
    }
}