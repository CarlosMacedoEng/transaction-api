package com.pismo.assessment.transaction_api.infrastructure;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.AccountEntity;
import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository.AccountRepository;
import com.pismo.assessment.transaction_api.infrastructure.adapters.web.dto.CreateTransactionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class TransactionControllerIT {

    @Container
    @ServiceConnection
    @SuppressWarnings({"unused", "deprecation"})
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        accountRepository.deleteAll();
        accountRepository.save(new AccountEntity(null, "12345678900"));
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {
        Long accountId = accountRepository.findAll().get(0).getId();

        CreateTransactionRequest request = new CreateTransactionRequest(
            accountId, 
            1, 
            new BigDecimal("123.45")
        );

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(-123.45))
                .andExpect(jsonPath("$.transaction_id").exists());
    }

    @Test
    void shouldReturn422WhenAccountDoesNotExist() throws Exception {
        CreateTransactionRequest request = new CreateTransactionRequest(
            9999L, 
            4, 
            BigDecimal.TEN
        );

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.error").value("Account not found"));
    }
    
}