package org.banksystem.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 *  strategy + observer
 * clase abstracta base de cuentas bancarias
 * se implementa el patrón observer para notificaciones
 * y usa Strategy para cálculo de intereses
 */
public abstract class Account {

    protected BigDecimal balance;
    protected InterestStrategy interestStrategy;
    protected final List<Notification> observers;
    protected final List<Transaction> transactions;

    public Account() {
        this.balance = BigDecimal.ZERO;
        this.observers = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }


    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "$0";
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        String formatted = format.format(amount);
        formatted = formatted.replace("COP", "").trim();
        return formatted.replace(",00", "");
    }

    /**
     * agrega un observador para recibir notificaciones de la cuenta
     */
    public void addObserver(Notification n) {
        if (n != null) observers.add(n);
    }

    /**
     * notifica a todos los observadores con un mensaje
     */
    public void notifyAllObservers(String message) {
        for (Notification n : observers) n.update(message);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }

    /**
     * deposita dinero en la cuent
     */
    public boolean deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            notifyAllObservers("Intento de depósito inválido: el monto debe ser mayor que 0");
            return false;
        }
        balance = balance.add(amount);
        transactions.add(new Transaction("Depósito", amount, "Depósito en cuenta"));
        notifyAllObservers("Depósito realizado por " + formatMoney(amount)
                + ". Nuevo saldo: " + formatMoney(balance));
        return true;
    }

    /**
     * retira dinero de la cuenta si hay fondos suficientes
     */
    public boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            notifyAllObservers("Intento de retiro inválido: el monto debe ser mayor que 0");
            return false;
        }
        if (balance.compareTo(amount) < 0) {
            notifyAllObservers("Retiro fallido: fondos insuficientes");
            return false;
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction("Retiro", amount, "Retiro de cuenta"));
        notifyAllObservers("Retiro realizado por " + formatMoney(amount)
                + ". Nuevo saldo: " + formatMoney(balance));
        return true;
    }

    /**
     * transfiere dinero a otra cuenta
     */
    public boolean transfer(BigDecimal amount, Account destination) {
        if (destination == null) {
            notifyAllObservers("Transferencia fallida: cuenta destino inválida");
            return false;
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            notifyAllObservers("Transferencia fallida: monto inválido");
            return false;
        }
        if (balance.compareTo(amount) < 0) {
            notifyAllObservers("Transferencia fallida: fondos insuficientes");
            return false;
        }
        balance = balance.subtract(amount);
        destination.balance = destination.balance.add(amount);

        this.transactions.add(new Transaction("Transferencia enviada", amount, "Transferencia a otra cuenta"));
        destination.transactions.add(new Transaction("Transferencia recibida", amount, "Transferencia recibida"));

        notifyAllObservers("Transferencia enviada por " + formatMoney(amount)
                + ". Nuevo saldo: " + formatMoney(balance));
        destination.notifyObserversFromTransfer("Ha recibido una transferencia de " + formatMoney(amount)
                + ". Nuevo saldo: " + formatMoney(destination.balance));

        return true;
    }
    public void notifyObserversFromTransfer(String message) {
        notifyAllObservers(message);
    }

    /**
     * asigna una estrategia de cálculo de interes
     */
    public void setInterestStrategy(InterestStrategy s) {
        this.interestStrategy = s;
    }

    /**
     * aplica el interés según la estrategia seleccionada
     */
    public void applyInterest() {
        if (interestStrategy == null) return;
        BigDecimal interest = interestStrategy.calculateInterest(balance);
        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(interest);
            transactions.add(new Transaction("Interés", interest, "Interés aplicado"));
            notifyAllObservers("Interés aplicado: " + formatMoney(interest)
                    + ". Nuevo saldo: " + formatMoney(balance));
        }
    }

    /**
     * metodo abstracto que define el tipo de cuenta ahorros, corriente
     */
    public abstract String getType();
}
