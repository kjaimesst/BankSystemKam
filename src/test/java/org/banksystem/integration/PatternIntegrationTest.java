package org.banksystem.integration;

import org.banksystem.controller.*;
import org.banksystem.model.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas de integración enfocadas en validar patrones de diseño
 */
class PatternIntegrationTest {

    @BeforeEach
    void setUp() {
        BankCoreSingleton.getInstance().clearForTests();
    }

    @Test
    @DisplayName("Pattern: Chain of Responsibility - Validadores en cadena")
    void testChainOfResponsibility_ValidatorsInChain() {
        Account account = AccountFactory.createAccount("Ahorros");
        account.deposit(new BigDecimal("1000"));

        // Crear cadena de validadores
        TransactionHandler limitValidator = new LimitValidator();
        TransactionHandler balanceValidator = new BalanceValidator();
        limitValidator.setNextHandler(balanceValidator);

        // Retiro válido (pasa ambos validadores)
        boolean resultValid = limitValidator.handle(account, new BigDecimal("-500"));
        assertTrue(resultValid, "Debe pasar la validación con fondos suficientes");

        // Retiro inválido (falla balance validator)
        boolean resultInvalid = limitValidator.handle(account, new BigDecimal("-2000"));
        assertFalse(resultInvalid, "Debe fallar la validación sin fondos suficientes");
    }

    @Test
    @DisplayName("Pattern: Command - Ejecución de comandos")
    void testCommandPattern_ExecuteCommands() {
        Account account = AccountFactory.createAccount("Ahorros");
        TransactionManagerInvoker invoker = new TransactionManagerInvoker();

        BigDecimal initialBalance = account.getBalance();

        // Ejecutar comando de depósito
        Command depositCommand = new DepositCommand(account, new BigDecimal("1000"));
        invoker.executeCommand(depositCommand);

        assertEquals(initialBalance.add(new BigDecimal("1000")), account.getBalance(),
            "El balance debe incrementarse después del depósito");

        // Ejecutar comando de retiro
        Command withdrawCommand = new WithdrawCommand(account, new BigDecimal("300"));
        invoker.executeCommand(withdrawCommand);

        assertEquals(initialBalance.add(new BigDecimal("700")), account.getBalance(),
            "El balance debe decrementarse después del retiro");
    }

    @Test
    @DisplayName("Pattern: Factory - Creación de diferentes tipos de cuentas")
    void testFactoryPattern_CreateDifferentAccounts() {
        Account savings = AccountFactory.createAccount("Ahorros");
        Account checking = AccountFactory.createAccount("Corriente");
        Account defaultAccount = AccountFactory.createAccount("Unknown");

        assertInstanceOf(SavingsAccount.class, savings, 
            "Factory debe crear SavingsAccount para 'Ahorros'");
        assertInstanceOf(CheckingAccount.class, checking,
            "Factory debe crear CheckingAccount para 'Corriente'");
        assertInstanceOf(SavingsAccount.class, defaultAccount,
            "Factory debe crear SavingsAccount por defecto");
    }

    @Test
    @DisplayName("Pattern: Singleton - Una única instancia de BankCore")
    void testSingletonPattern_OnlyOneInstance() {
        BankCoreSingleton instance1 = BankCoreSingleton.getInstance();
        BankCoreSingleton instance2 = BankCoreSingleton.getInstance();

        assertSame(instance1, instance2, "Debe retornar la misma instancia");

        // Registrar cliente en instance1
        instance1.registerCustomer("Test User", "test@bank.com", "Ahorros");

        // Verificar que se ve en instance2
        assertEquals(1, instance2.getCustomers().size(),
            "Ambas referencias deben apuntar al mismo objeto");
    }

    @Test
    @DisplayName("Pattern: Strategy - Diferentes estrategias de interés")
    void testStrategyPattern_InterestStrategies() {
        // Estrategia estándar
        InterestStrategy standardStrategy = new StandardInterest();
        BigDecimal standardInterest = standardStrategy.calculateInterest(new BigDecimal("10000"));
        assertTrue(standardInterest.compareTo(BigDecimal.ZERO) > 0,
            "Interés estándar debe ser mayor a cero");

        // Estrategia premium
        InterestStrategy premiumStrategy = new PremiumInterest();
        BigDecimal premiumInterest = premiumStrategy.calculateInterest(new BigDecimal("10000"));
        assertTrue(premiumInterest.compareTo(standardInterest) > 0,
            "Interés premium debe ser mayor que el estándar");

        // Verificar que se pueden calcular intereses con diferentes estrategias
        assertNotNull(standardInterest, "Debe calcular interés estándar");
        assertNotNull(premiumInterest, "Debe calcular interés premium");
    }

    @Test
    @DisplayName("Pattern: Observer - Notificaciones a observadores")
    void testObserverPattern_Notifications() {
        Account account = AccountFactory.createAccount("Ahorros");
        
        // Crear observer mock
        Notification mockNotification = mock(Notification.class);
        account.addObserver(mockNotification);

        // Realizar operación que debe notificar
        account.deposit(new BigDecimal("1000"));

        // Verificar que se notificó al observador
        verify(mockNotification, atLeastOnce()).update(anyString());
    }

    @Test
    @DisplayName("Pattern: Facade - Simplificación de operaciones complejas")
    void testFacadePattern_SimplifiedOperations() {
        BankFacade facade = new BankFacade();
        
        // El facade debe simplificar operaciones complejas
        Customer customer = facade.registerCustomer("Facade Test", "facade@bank.com", "Ahorros");
        assertNotNull(customer, "Facade debe registrar cliente correctamente");

        Account account = customer.getAccount();
        facade.depositMoney(account, new BigDecimal("5000"));
        
        assertEquals(new BigDecimal("5000"), account.getBalance(),
            "Facade debe manejar depósito con validadores internamente");
    }

    @Test
    @DisplayName("Integration: Transferencia con validadores y comandos")
    void testFullIntegration_TransferWithValidatorsAndCommands() {
        BankFacade facade = new BankFacade();
        
        Customer sender = facade.registerCustomer("Sender", "sender@bank.com", "Ahorros");
        Customer receiver = facade.registerCustomer("Receiver", "receiver@bank.com", "Ahorros");

        Account senderAccount = sender.getAccount();
        Account receiverAccount = receiver.getAccount();

        // Depositar fondos al emisor
        facade.depositMoney(senderAccount, new BigDecimal("3000"));

        BigDecimal senderInitial = senderAccount.getBalance();
        BigDecimal receiverInitial = receiverAccount.getBalance();

        // Transferir (usa validadores y comando)
        facade.transferMoney(senderAccount, receiverAccount, new BigDecimal("1500"));

        assertEquals(senderInitial.subtract(new BigDecimal("1500")), senderAccount.getBalance(),
            "El emisor debe tener menos fondos");
        assertEquals(receiverInitial.add(new BigDecimal("1500")), receiverAccount.getBalance(),
            "El receptor debe tener más fondos");
    }

    @Test
    @DisplayName("Integration: Validación de límites en operaciones")
    void testLimitValidation_InOperations() {
        Account account = AccountFactory.createAccount("Ahorros");
        account.deposit(new BigDecimal("100000"));

        TransactionHandler limitValidator = new LimitValidator();

        // Depósito dentro del límite (10 millones)
        boolean validDeposit = limitValidator.handle(account, new BigDecimal("5000"));
        assertTrue(validDeposit, "Depósito dentro del límite debe ser válido");

        // Depósito excediendo límite (más de 10 millones)
        boolean invalidDeposit = limitValidator.handle(account, new BigDecimal("15000000"));
        assertFalse(invalidDeposit, "Depósito excediendo límite de 10 millones debe ser rechazado");
    }
}
