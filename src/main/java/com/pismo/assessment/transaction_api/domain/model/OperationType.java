package com.pismo.assessment.transaction_api.domain.model;

public enum OperationType {
    PURCHASE(1),
    INSTALLMENT_PURCHASE(2),
    WITHDRAWAL(3),
    PAYMENT(4);

    private final int id;

    OperationType(int id) {
        this.id = id;
    }
    
    public static OperationType fromId(int id) {
        for (OperationType type : values()) {
            if (type.id == id) return type;
        }
        
        throw new IllegalArgumentException("Invalid Operation Type: " + id);
    }

    public Integer getId() {
        return this.id;
    }
    
}
