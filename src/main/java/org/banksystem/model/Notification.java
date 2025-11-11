package org.banksystem.model;

/**
 * Interfaz de notificación (Observer).
 * Implementaciones pueden enviar notificaciones por consola, email, SMS, etc.
 */
public interface Notification {
    void update(String message);
}
