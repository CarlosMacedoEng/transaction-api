package com.pismo.assessment.transaction_api.application.port.in;

import java.math.BigDecimal;

import com.pismo.assessment.transaction_api.domain.model.Transaction;

public interface CreateTransactionUseCase {
    
    Transaction create(Long accountId, Integer operationTypeId, BigDecimal amount);

}
