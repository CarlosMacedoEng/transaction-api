package com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pismo.assessment.transaction_api.application.port.in.CreateAccountUseCase;
import com.pismo.assessment.transaction_api.application.port.in.GetAccountUseCase;
import com.pismo.assessment.transaction_api.domain.model.Account;

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
    
}
