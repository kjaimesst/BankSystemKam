package org.banksystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * registro: clase de apoyo que registra cada operacion (deposito, retiro, transferencia, interes)
 */
public class Transaction {
    private final String id;
    private final String type;
    private final BigDecimal amount;
    private final String description;
    private final LocalDateTime timestamp;

    public Transaction(String type, BigDecimal amount, String description) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "[" + timestamp.format(f) + "] " + type + " - $" + amount + " (" + description + ")";
    }

    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}
