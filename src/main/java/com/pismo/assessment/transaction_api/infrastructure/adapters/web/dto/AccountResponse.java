package com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pismo.assessment.transaction_api.domain.model.Account;

public record AccountResponse(
    @JsonProperty("account_id") Long accountId,
    @JsonProperty("document_number") String documentNumber
) {
    
    public static AccountResponse fromDomain(Account account) {
        return new AccountResponse(account.getId(), account.getDocumentNumber());
    }
    
}
