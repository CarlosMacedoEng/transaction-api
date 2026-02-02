package com.pismo.assessment.transaction_api.infrastructure.adapters.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pismo.assessment.transaction_api.application.port.out.LoadAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveTransactionPort;
import com.pismo.assessment.transaction_api.domain.model.Account;
import com.pismo.assessment.transaction_api.domain.model.OperationType;
import com.pismo.assessment.transaction_api.domain.model.Transaction;
import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository.AccountRepository;
import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersistenceAdapter implements LoadAccountPort, SaveTransactionPort, SaveAccountPort {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    @Override
    public Transaction save(Transaction transaction) {
        // Convert Domain -> JPA Entity
        TransactionEntity entity = TransactionEntity.builder()
                .accountId(transaction.getAccountId())
                .operationTypeId(transaction.getOperationType().getId())
                .amount(transaction.getAmount())
                .eventDate(transaction.getEventDate())
                .build();

        TransactionEntity savedEntity = transactionRepository.save(entity);

        // 3. Convert JPA Entity -> Domain
        return new Transaction(
                savedEntity.getId(),
                savedEntity.getAccountId(),
                OperationType.fromId(savedEntity.getOperationTypeId()),
                savedEntity.getAmount(),
                savedEntity.getEventDate()
        );
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id)
                .map(entity -> new Account(entity.getId(), entity.getDocumentNumber()));
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = new AccountEntity(account.getId(), account.getDocumentNumber());
        AccountEntity saved = accountRepository.save(entity);
        
        return new Account(saved.getId(), saved.getDocumentNumber());
    }
    
}
