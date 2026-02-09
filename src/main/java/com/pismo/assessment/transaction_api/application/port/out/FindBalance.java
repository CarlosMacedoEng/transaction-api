package com.pismo.assessment.transaction_api.application.port.out;

import java.util.List;

import com.pismo.assessment.transaction_api.domain.model.Transaction;

public interface FindBalance {

    // Achar operações de débito
    List<Transaction> findOpenDebits(Long accountId);


}
