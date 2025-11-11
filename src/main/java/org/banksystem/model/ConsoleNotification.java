package org.banksystem.model;

/**
 * Implementación simple que imprime la notificación en consola
 */
public class ConsoleNotification implements Notification {
    private final String ownerName;

    public ConsoleNotification(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public void update(String message) {
        System.out.println(" Notificación para " + ownerName + ": " + message);
    }
}


