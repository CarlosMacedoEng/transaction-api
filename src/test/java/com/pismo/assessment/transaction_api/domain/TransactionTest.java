package com.pismo.assessment.transaction_api.domain;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pismo.assessment.transaction_api.domain.model.OperationType;
import com.pismo.assessment.transaction_api.domain.model.Transaction;

class TransactionTest {

    @Test
    @DisplayName("Should create debt transaction with negative amount when operation is PURCHASE (1)")
    void shouldCreateNegativeAmountForPurchase() {
        
        Long accountId = 1L;
        BigDecimal inputAmount = new BigDecimal("100.00");

        
        Transaction transaction = new Transaction(accountId, OperationType.PURCHASE, inputAmount);

        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("-100.00"));
    }

    @Test
    @DisplayName("Should create credit transaction with positive amount when operation is PAYMENT (4)")
    void shouldCreatePositiveAmountForPayment() {
        
        Long accountId = 1L;
        BigDecimal inputAmount = new BigDecimal("50.00");

        
        Transaction transaction = new Transaction(accountId, OperationType.PAYMENT, inputAmount);

        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Should negate amount even if input is already negative for debt operations")
    void shouldHandleNegativeInputRobustly() {
        
        Transaction transaction = new Transaction(1L, OperationType.WITHDRAWAL, new BigDecimal("-20.00"));
        
        assertThat(transaction.getAmount()).isEqualByComparingTo(new BigDecimal("-20.00"));
    }
}
