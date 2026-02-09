package com.pismo.assessment.transaction_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class Transaction {
    private Long id;
    private final Long accountId;
    private final OperationType operationType;
    private final BigDecimal amount;
    private final BigDecimal balance;
    private final LocalDateTime eventDate;

    // Constructor 1: CREATION (Used by CreateTransactionUseCase/Service)
    public Transaction(Long accountId, OperationType operationType, BigDecimal amount) {
        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = applySignRule(operationType, amount);
        this.balance = this.amount;
        this.eventDate = LocalDateTime.now();
    }

    public Transaction(Long accountId, OperationType operationType, BigDecimal amount, BigDecimal balance) {
        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = applySignRule(operationType, amount);
        this.balance = balance;
        this.eventDate = LocalDateTime.now();
    }

    // Constructor 3: RECONSTRUCTION (Used ONLY by the PersistenceAdapter when loading from the bank)
    public Transaction(Long id, Long accountId, OperationType operationType, BigDecimal amount, BigDecimal balance, LocalDateTime eventDate) {
        this.id = id;
        this.accountId = accountId;
        this.operationType = operationType;
        this.amount = amount;
        this.balance = balance; 
        this.eventDate = eventDate;
    }

    private BigDecimal applySignRule(OperationType type, BigDecimal value) {
        if (type == OperationType.PAYMENT) {
            return value.abs();
        }
        return value.abs().negate();
    }
    
}   