package com.pismo.assessment.transaction_api.application.port.out;

import java.util.Optional;

import com.pismo.assessment.transaction_api.domain.model.Account;

public interface LoadAccountPort {
    
    boolean existsById(Long accountId);
    
    Optional<Account> findById(Long id);
    
}
