package org.banksystem.junit;

import org.banksystem.model.BankCoreSingleton;
import org.banksystem.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas básicas para BankCoreSingleton
 */
class BankCoreSingletonTest {

    private BankCoreSingleton banco;

    @BeforeEach
    void setUp() {
        banco = BankCoreSingleton.getInstance();
        banco.clearForTests(); // Limpia la lista antes de cada prueba
    }

    @Test
    void getInstance_debeRetornarLaMismaInstancia() {
        BankCoreSingleton otro = BankCoreSingleton.getInstance();
        assertSame(banco, otro, "Debe ser la misma instancia (singleton)");
    }

    @Test
    void registerCustomer_creaNuevoCliente() {
        Customer cliente = banco.registerCustomer("Oriana", "ori@correo.com", "ahorros");
        assertNotNull(cliente, "El cliente no debe ser nulo");
        assertEquals("ori@correo.com", cliente.getEmail());
        List<Customer> clientes = banco.getCustomers();
        assertEquals(1, clientes.size(), "Debe haber un cliente registrado");
    }

    @Test
    void registerCustomer_noPermiteEmailDuplicado() {
        Customer c1 = banco.registerCustomer("Oriana", "ori@correo.com", "ahorros");
        Customer c2 = banco.registerCustomer("Mario", "ori@correo.com", "corriente");

        assertSame(c1, c2, "Debe devolver el cliente existente si el correo ya está registrado");
        assertEquals(1, banco.getCustomers().size(), "Debe haber solo un cliente");
    }

    @Test
    void registerCustomer_emailInvalidoDevuelveNull() {
        Customer c = banco.registerCustomer("Oriana", "  ", "ahorros");
        assertNull(c, "Debe retornar null si el correo es inválido");
        assertTrue(banco.getCustomers().isEmpty(), "No debe agregar ningún cliente");
    }

    @Test
    void findCustomerByEmail_encuentraClienteExistente() {
        banco.registerCustomer("Oriana", "ori@correo.com", "ahorros");
        Customer encontrado = banco.findCustomerByEmail("ORI@CORREO.COM");
        assertNotNull(encontrado);
        assertEquals("ori@correo.com", encontrado.getEmail());
    }

    @Test
    void findCustomerByEmail_retornaNullSiNoExiste() {
        Customer c = banco.findCustomerByEmail("noexiste@mail.com");
        assertNull(c);
    }
}