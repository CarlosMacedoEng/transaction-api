package com.pismo.assessment.transaction_api.application.port.out;

import com.pismo.assessment.transaction_api.domain.model.Transaction;

public interface SaveTransactionPort {

    Transaction save(Transaction transaction);

}
