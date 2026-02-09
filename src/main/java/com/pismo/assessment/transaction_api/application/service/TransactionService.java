package com.pismo.assessment.transaction_api.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pismo.assessment.transaction_api.application.port.in.CreateTransactionUseCase;
import com.pismo.assessment.transaction_api.application.port.in.GetTransactionsByAcountUseCase;
import com.pismo.assessment.transaction_api.application.port.out.FindBalance;
import com.pismo.assessment.transaction_api.application.port.out.LoadAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.LoadTransactionPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveTransactionPort;
import com.pismo.assessment.transaction_api.application.port.out.UpdateBalance;
import com.pismo.assessment.transaction_api.domain.exception.AccountNotFoundException;
import com.pismo.assessment.transaction_api.domain.model.OperationType;
import com.pismo.assessment.transaction_api.domain.model.Transaction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements CreateTransactionUseCase, GetTransactionsByAcountUseCase {

    private final LoadAccountPort loadAccountPort;
    private final LoadTransactionPort loadTransactionPort;
    private final SaveTransactionPort saveTransactionPort;
    private final FindBalance findBalance;
    private final UpdateBalance updateBalance;

    @Override
    @Transactional
    public Transaction create(Long accountId, Integer operationTypeId, BigDecimal amount) {
        // Business Validation (Check if the document already exists)
        if (!loadAccountPort.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }

        OperationType type = OperationType.fromId(operationTypeId);

        // Business Validation (Negative sign rule applies)
        // Transaction transaction = new Transaction(accountId, type, amount);

        if (type != OperationType.PAYMENT) {
            Transaction transaction = new Transaction(accountId, type, amount);
            return saveTransactionPort.save(transaction);
        }

        BigDecimal remaining = amount.abs();
        List<Transaction> openDebits = findBalance.findOpenDebits(accountId);
        Map<Long, BigDecimal> updates = new HashMap<>();

        for (Transaction debit : openDebits) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal absBalance = debit.getBalance().abs();
            BigDecimal paid = absBalance.min(remaining);
            BigDecimal newBalance = debit.getBalance().add(paid);

            updates.put(debit.getId(), newBalance);
            remaining = remaining.subtract(paid);
        }

        updateBalance.updateBalance(updates);

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remaining = BigDecimal.ZERO;
        }

        Transaction payment = new Transaction(accountId, OperationType.PAYMENT, amount, remaining);
        return saveTransactionPort.save(payment);
    }

    @Override
    public Page<Transaction> getByAccount(Long accountId, LocalDateTime from, LocalDateTime to, Integer operationTypeId, int page, int size) {
        
        if (!loadAccountPort.existsById(accountId)) {
            throw new AccountNotFoundException(accountId);
        }

        var pageable = PageRequest.of(page, size);

        return loadTransactionPort.findByAccount(accountId, from, to, operationTypeId, pageable);

    }


}
