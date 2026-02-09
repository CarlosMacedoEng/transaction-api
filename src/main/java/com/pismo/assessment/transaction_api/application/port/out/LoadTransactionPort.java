package com.pismo.assessment.transaction_api.application.port.out;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pismo.assessment.transaction_api.domain.model.Transaction;

public interface LoadTransactionPort {

    Page<Transaction> findByAccount(
        Long accountId,
        LocalDateTime from,
        LocalDateTime to,
        Integer operationTypeId,
        Pageable pageable
    );

}
