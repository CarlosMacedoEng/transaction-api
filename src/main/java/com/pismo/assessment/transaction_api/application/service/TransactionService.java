package com.pismo.assessment.transaction_api.application.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.pismo.assessment.transaction_api.application.port.in.CreateTransactionUseCase;
import com.pismo.assessment.transaction_api.application.port.out.LoadAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveTransactionPort;
import com.pismo.assessment.transaction_api.domain.exception.AccountNotFoundException;
import com.pismo.assessment.transaction_api.domain.model.OperationType;
import com.pismo.assessment.transaction_api.domain.model.Transaction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements CreateTransactionUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveTransactionPort saveTransactionPort;

    @Override
    @Transactional
    public Transaction create(Long accountId, Integer operationTypeId, BigDecimal amount) {
        // Business Validation (Check if the document already exists)
        if (!loadAccountPort.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }

        OperationType type = OperationType.fromId(operationTypeId);

        // Business Validation (Negative sign rule applies)
        Transaction transaction = new Transaction(accountId, type, amount);

        return saveTransactionPort.save(transaction);
    }
    
}
