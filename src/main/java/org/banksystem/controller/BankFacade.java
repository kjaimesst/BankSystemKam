package org.banksystem.controller;

import org.banksystem.model.*;
import java.math.BigDecimal;

/**
 * Display name: BankFacade — Facade
 * Simplifica el uso del sistema bancario, ofreciendo métodos directos
 * para las operaciones más comunes.
 */
public class BankFacade {
    private final BankCoreSingleton core = BankCoreSingleton.getInstance();
    private final TransactionManagerInvoker invoker = new TransactionManagerInvoker();

    public Customer registerCustomer(String name, String email, String accountType) {
        return core.registerCustomer(name, email, accountType);
    }

    public void depositMoney(Account account, BigDecimal amount) {
        TransactionHandler limitValidator = new LimitValidator();
        TransactionHandler balanceValidator = new BalanceValidator();
        limitValidator.setNextHandler(balanceValidator);

        if (limitValidator.handle(account, amount)) {
            Command command = new DepositCommand(account, amount);
            invoker.executeCommand(command);
        }
    }

    public void withdrawMoney(Account account, BigDecimal amount) {
        TransactionHandler limitValidator = new LimitValidator();
        TransactionHandler balanceValidator = new BalanceValidator();
        limitValidator.setNextHandler(balanceValidator);

        if (limitValidator.handle(account, amount)) {
            Command command = new WithdrawCommand(account, amount);
            invoker.executeCommand(command);
        }
    }

    public void transferMoney(Account from, Account to, BigDecimal amount) {
        TransactionHandler limitValidator = new LimitValidator();
        TransactionHandler balanceValidator = new BalanceValidator();
        limitValidator.setNextHandler(balanceValidator);

        if (limitValidator.handle(from, amount)) {
            Command command = new TransferCommand(from, to, amount);
            invoker.executeCommand(command);
        }
    }

    public void listCustomers() {
        core.listCustomers();
    }
}
