package org.banksystem.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *  loan: rpresenta un préstamo dentro del sistema bancario
 * se comunica con la cuenta del cliente y envía notificaciones usando el patrón observer
 */
public class Loan {
    private final Customer owner;
    private BigDecimal outstanding; // deuda total pendiente

    public Loan(Customer owner) {
        this.owner = owner;
        this.outstanding = BigDecimal.ZERO;
    }


    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "$0";
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        String formatted = format.format(amount);
        formatted = formatted.replace("COP", "").trim();
        return formatted.replace(",00", "");
    }

    // aprueba un prestamo y deposita el dinero en la cuenta del client

    public boolean approveLoan(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            owner.getAccount().notifyAllObservers("Solicitud de préstamo inválida: monto debe ser mayor que 0");
            return false;
        }

        // aumenta la deuda y deposita el prestamo
        outstanding = outstanding.add(amount);
        owner.getAccount().deposit(amount);

        owner.getAccount().notifyAllObservers("Préstamo aprobado por "
                + formatMoney(amount)
                + ". Deuda total actual: " + formatMoney(outstanding));
        return true;
    }

    /**
     * permite realizar pagos parciales o totales al prestamo
     */
    public boolean makePayment(BigDecimal payment) {
        if (payment == null || payment.compareTo(BigDecimal.ZERO) <= 0) {
            owner.getAccount().notifyAllObservers("Pago inválido: el monto debe ser mayor que 0");
            return false;
        }
        if (outstanding.compareTo(BigDecimal.ZERO) <= 0) {
            owner.getAccount().notifyAllObservers("No hay deuda pendiente por pagar");
            return false;
        }
        if (payment.compareTo(outstanding) > 0) {
            payment = outstanding;
        }

        outstanding = outstanding.subtract(payment);
        owner.getAccount().withdraw(payment);

        owner.getAccount().notifyAllObservers("Pago realizado por "
                + formatMoney(payment)
                + ". Deuda restante: " + formatMoney(outstanding));
        return true;
    }

    // retorna la deuda actual

    public BigDecimal getOutstanding() {
        return outstanding;
    }
}

