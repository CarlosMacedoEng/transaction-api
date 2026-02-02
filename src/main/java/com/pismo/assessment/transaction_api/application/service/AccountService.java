package com.pismo.assessment.transaction_api.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pismo.assessment.transaction_api.application.port.in.CreateAccountUseCase;
import com.pismo.assessment.transaction_api.application.port.in.GetAccountUseCase;
import com.pismo.assessment.transaction_api.application.port.out.LoadAccountPort;
import com.pismo.assessment.transaction_api.application.port.out.SaveAccountPort;
import com.pismo.assessment.transaction_api.domain.exception.AccountNotFoundException;
import com.pismo.assessment.transaction_api.domain.model.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, GetAccountUseCase {

    private final SaveAccountPort saveAccountPort;
    private final LoadAccountPort loadAccountPort;

    @Override
    @Transactional
    public Account create(String documentNumber) {
        Account newAccount = new Account(documentNumber);
        
        return saveAccountPort.save(newAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getById(Long id) {
        return loadAccountPort.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }
    
}
