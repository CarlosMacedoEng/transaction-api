package com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Page<TransactionEntity> findByAccountIdAndEventDateBetween(Long accountId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<TransactionEntity> findByAccountIdAndEventDateBetweenAndOperationTypeId(Long accountId, LocalDateTime from, LocalDateTime to, Integer operationTypeId, Pageable pageable);

    List<TransactionEntity> findByAccountIdAndOperationTypeIdInAndBalanceLessThanOrderByEventDateAscIdAsc(
        Long accountId,
        List<Integer> operationTypeIds,
        BigDecimal balance
    );

}
