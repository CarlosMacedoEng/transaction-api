package com.pismo.assessment.transaction_api.application.port.in;

import com.pismo.assessment.transaction_api.domain.model.Account;

public interface GetAccountUseCase {

    Account getById(Long id);

}
