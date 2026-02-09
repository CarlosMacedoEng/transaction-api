package com.pismo.assessment.transaction_api.infrastructure.adapters.web;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pismo.assessment.transaction_api.application.port.in.CreateAccountUseCase;
import com.pismo.assessment.transaction_api.application.port.in.GetAccountUseCase;
import com.pismo.assessment.transaction_api.application.port.in.GetTransactionsByAcountUseCase;
import com.pismo.assessment.transaction_api.domain.model.Account;
import com.pismo.assessment.transaction_api.domain.model.Transaction;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.AccountResponse;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.CreateAccountRequest;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.PageResponse;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.TransactionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations related to account management")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final GetTransactionsByAcountUseCase getTransactionsByAcountUseCase;

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountResponse> create(@RequestBody @Valid CreateAccountRequest request) {
        Account created = createAccountUseCase.create(request.documentNumber());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AccountResponse.fromDomain(created));
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long accountId) {
        Account account = getAccountUseCase.getById(accountId);
        return ResponseEntity.ok(AccountResponse.fromDomain(account));
    }

    @GetMapping("/{accountId}/transactions")
    @Operation(summary = "Get account transactions by ID")
    public ResponseEntity<PageResponse<TransactionResponse>> getTransactions(
        @PathVariable Long accountId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(required = false, name = "operation_type_id") Integer operationTypeId
    ) {
        LocalDateTime effectiveFrom = (from != null) ? from : LocalDateTime.MIN;
        LocalDateTime effectiveTo = (to != null) ? to : LocalDateTime.now();

        Page<Transaction> result = getTransactionsByAcountUseCase.getByAccount(accountId, effectiveFrom, effectiveTo, operationTypeId, page, size);

        var content = result.getContent().stream()
            .map(TransactionResponse::fromDomain)
            .toList();
    
        var response = new PageResponse<>(
            content,
            result.getNumber(),
            result.getSize(),
            result.getTotalElements(),
            result.getTotalPages()
        );
        
        return ResponseEntity.ok(response);
    
    }
    
}
