package com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pismo.assessment.transaction_api.domain.model.Transaction;

public record TransactionResponse(
    @JsonProperty("transaction_id") Long transactionId,
    @JsonProperty("account_id") Long accountId,
    @JsonProperty("operation_type_id") Integer operationTypeId,
    BigDecimal amount,
    @JsonProperty("event_date") LocalDateTime eventDate
) {
    
    // Convert Domain -> DTO
    public static TransactionResponse fromDomain(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId(),
            transaction.getAccountId(),
            transaction.getOperationType().getId(),
             transaction.getAmount(),
             transaction.getEventDate()
        );
    }

}
