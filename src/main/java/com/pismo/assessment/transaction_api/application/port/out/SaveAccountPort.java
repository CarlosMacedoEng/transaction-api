package com.pismo.assessment.transaction_api.application.port.out;

import com.pismo.assessment.transaction_api.domain.model.Account;

public interface SaveAccountPort {
    
    Account save(Account account);
    
}
