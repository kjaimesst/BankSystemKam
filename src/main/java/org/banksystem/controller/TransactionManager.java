package org.banksystem.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Invoker: ejecuta comandos y guarda historial
 */
public class TransactionManager {
    private final List<Command> history = new ArrayList<>();

    public void executeCommand(Command cmd) {
        if (cmd == null) return;
        cmd.execute();
        history.add(cmd);
    }

    public void showHistory() {
        System.out.println("\nHistorial de comandos ejecutados:");
        for (Command c : history) {
            System.out.println("- " + c.toString());
        }
    }
}
