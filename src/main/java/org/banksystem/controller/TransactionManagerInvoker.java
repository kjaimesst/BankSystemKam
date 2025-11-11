package org.banksystem.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Display name: TransactionManagerInvoker — Invoker
 * Ejecuta comandos y mantiene un historial de operaciones.
 */
public class TransactionManagerInvoker {
    private final List<Command> history = new ArrayList<>();

    public void executeCommand(Command command) {
        command.execute();
        history.add(command);
    }

    public void showHistory() {
        System.out.println("Historial de operaciones ejecutadas: " + history.size());
    }
}
