package com.pismo.assessment.transaction_api.domain.model;

import lombok.Getter;

@Getter
public class Account {
    private final Long id;
    private final String documentNumber;

    public Account(String documentNumber) {
        this.id = null;
        this.documentNumber = documentNumber;
    }

    public Account(Long id, String documentNumber) {
        this.id = id;
        this.documentNumber = documentNumber;
    }
}
