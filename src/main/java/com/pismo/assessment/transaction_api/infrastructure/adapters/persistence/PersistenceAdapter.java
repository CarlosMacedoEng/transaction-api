package com.pismo.assessment.transaction_api.infrastructure.adapters.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.pismo.assessment.transaction_api.application.port.out.FindBalance;
import com.pismo.assessment.transaction_api.application.port.out.LoadAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.LoadTransactionPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveTransactionPort;
import com.pismo.assessment.transaction_api.application.port.out.UpdateBalance;
import com.pismo.assessment.transaction_api.domain.model.Account;
import com.pismo.assessment.transaction_api.domain.model.OperationType;
import com.pismo.assessment.transaction_api.domain.model.Transaction;
import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository.AccountRepository;
import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersistenceAdapter implements 
    LoadAccountPort, 
    SaveTransactionPort, 
    SaveAccountPort, 
    LoadTransactionPort,
    UpdateBalance, 
    FindBalance {

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
                .balance(transaction.getBalance())
                .eventDate(transaction.getEventDate())
                .build();

        TransactionEntity savedEntity = transactionRepository.save(entity);

        // 3. Convert JPA Entity -> Domain
        return new Transaction(
                savedEntity.getId(),
                savedEntity.getAccountId(),
                OperationType.fromId(savedEntity.getOperationTypeId()),
                savedEntity.getAmount(),
                savedEntity.getBalance(),
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

    @Override
    public Page<Transaction> findByAccount(Long accountId, LocalDateTime from, LocalDateTime to,
                                           Integer operationTypeId, Pageable pageable) {

        Page<TransactionEntity> page;

        if (operationTypeId == null) {
            page = transactionRepository.findByAccountIdAndEventDateBetween(accountId, from, to, pageable);
        } else {
            page = transactionRepository.findByAccountIdAndEventDateBetweenAndOperationTypeId(
                accountId, from, to, operationTypeId, pageable
            );
        }

        return page.map(entity -> new Transaction(
            entity.getId(),
            entity.getAccountId(),
            OperationType.fromId(entity.getOperationTypeId()),
            entity.getAmount(),
            entity.getBalance(),
            entity.getEventDate()
        ));
    }

    @Override
    public List<Transaction> findOpenDebits(Long accountId) {
        List<TransactionEntity> entities =
            transactionRepository.findByAccountIdAndOperationTypeIdInAndBalanceLessThanOrderByEventDateAscIdAsc(
                accountId,
                List.of(
                    OperationType.PURCHASE.getId(),
                    OperationType.INSTALLMENT_PURCHASE.getId(),
                    OperationType.WITHDRAWAL.getId()
                ),
                BigDecimal.ZERO
            );

        return entities.stream()
            .map(entity -> new Transaction(
                entity.getId(),
                entity.getAccountId(),
                OperationType.fromId(entity.getOperationTypeId()),
                entity.getAmount(),
                entity.getBalance(),
                entity.getEventDate()
            ))
            .toList();
    }

    @Override
    public void updateBalance(Map<Long, BigDecimal> balanceById) {
        if (balanceById.isEmpty()) {
            return;
        }

        List<TransactionEntity> entities = transactionRepository.findAllById(balanceById.keySet());

        for (TransactionEntity entity : entities) {
            BigDecimal newBalance = balanceById.get(entity.getId());
            entity.setBalance(newBalance);
        }

        transactionRepository.saveAll(entities);
    }   

}
