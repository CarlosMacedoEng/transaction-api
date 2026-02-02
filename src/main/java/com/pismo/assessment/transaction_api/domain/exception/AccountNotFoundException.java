package com.pismo.assessment.transaction_api.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(Long id) {
        super("Account not found with ID: " + id);
    }

}
