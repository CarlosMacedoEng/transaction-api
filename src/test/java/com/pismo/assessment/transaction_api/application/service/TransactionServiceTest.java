package com.pismo.assessment.transaction_api.application.service;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pismo.assessment.transaction_api.application.port.out.LoadAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveTransactionPort;
import com.pismo.assessment.transaction_api.domain.exception.AccountNotFoundException;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private LoadAccountPort loadAccountPort;

    @Mock
    private SaveTransactionPort saveTransactionPort;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Should throw AccountNotFoundException when account does not exist")
    void shouldThrowExceptionWhenAccountNotFound() {
        
        Long invalidAccountId = 999L;
        when(loadAccountPort.existsById(invalidAccountId)).thenReturn(false);

        
        assertThatThrownBy(() -> 
            transactionService.create(invalidAccountId, 4, BigDecimal.TEN)
        )
        .isInstanceOf(AccountNotFoundException.class)
        .hasMessageContaining("999");

        verify(saveTransactionPort, never()).save(any());
    }
}
