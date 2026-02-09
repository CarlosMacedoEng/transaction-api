package com.pismo.assessment.transaction_api.application.port.out;

import java.math.BigDecimal;
import java.util.Map;

public interface UpdateBalance {

    void updateBalance(Map<Long, BigDecimal> balanceById);

}
