package org.banksystem.model;

/**
 * interfaz observer
 * implementaciones pueden enviar notificaciones por consola, email, SMS y dmas
 */

    public interface Notification {
        void update(String message);
}
