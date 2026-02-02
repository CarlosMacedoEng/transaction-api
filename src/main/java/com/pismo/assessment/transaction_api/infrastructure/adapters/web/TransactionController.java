package com.pismo.assessment.transaction_api.infrastructure.adapters.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pismo.assessment.transaction_api.application.port.in.CreateTransactionUseCase;
import com.pismo.assessment.transaction_api.domain.model.Transaction;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.CreateTransactionRequest;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.TransactionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Operations related to transaction management")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateTransactionRequest request) {
        
        Transaction createdTransaction = createTransactionUseCase.create(
            request.accountId(),
            request.operationTypeId(),
            request.amount()
        );

        // Convertion Domain -> DTO
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TransactionResponse.fromDomain(createdTransaction));
    }
}
