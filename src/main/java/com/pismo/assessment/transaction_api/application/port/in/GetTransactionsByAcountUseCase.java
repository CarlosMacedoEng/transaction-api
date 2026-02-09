package com.pismo.assessment.transaction_api.application.port.in;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.pismo.assessment.transaction_api.domain.model.Transaction;

public interface GetTransactionsByAcountUseCase {

    Page<Transaction> getByAccount(
        Long accountId,
        LocalDateTime from,
        LocalDateTime to,
        Integer operationTypeId,
        int page,
        int size
    );

}
