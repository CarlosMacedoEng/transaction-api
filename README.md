# Transaction API - Pismo Code Assessment

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple)

## 📋 Project Overview

This project is a RESTful API designed to manage customer accounts and financial transactions. It was developed as part of the Pismo technical assessment.

The application handles the creation of accounts and the processing of different transaction types (purchases, withdrawals, and payments), strictly following business rules regarding credit/debt representation.

## 🚀 Key Features

* **Account Management:** Create and retrieve customer accounts.
* **Transaction Processing:** Records transactions ensuring data consistency.
* **Automatic Sign Conversion:**
    * **Credit (Positive):** Payments (`OperationType: 4`).
    * **Debt (Negative):** Purchase (`1`), Installment Purchase (`2`), Withdrawal (`3`).
* **Idempotency & Integrity:** Prevents invalid operations and ensures account existence validation.

## 🛠️ Tech Stack & Architecture

This project implements **Hexagonal Architecture (Ports and Adapters)** to ensure the Domain Logic remains isolated from frameworks and external agents.

* **Language:** Java 17 (LTS)
* **Framework:** Spring Boot 3
* **Database:** PostgreSQL (Production/Docker) and H2 (Tests).
* **Infrastructure:** Docker & Docker Compose.
* **Documentation:** OpenAPI / Swagger UI.
* **Testing:**
    * **JUnit 5 & Mockito:** For unit testing domain rules and services.
    * **Testcontainers:** For integration testing using real PostgreSQL instances in Docker.

### Project Structure
```text
src/main/java/com/pismo/assessment/transaction_api
│
├── application                # Application Business Rules
│   ├── port                   # Input/Output Interfaces (Ports)
│   └── service                # Use Case Implementations
│
├── domain                     # Core Domain Logic (Pure Java)
│   ├── model                  # Rich Entities (Account, Transaction)
│   └── exception              # Domain Exceptions
│
└── infrastructure             # Frameworks & Drivers (Adapters)
    ├── adapters
    │   ├── web                # REST Controllers (Input Adapters)
    │   └── persistence        # JPA Repositories (Output Adapters)
    └── configuration          # Spring Beans & Config

src/test/java/com/pismo/assessment/transaction_api
│
├── application                # Unit Tests (Mockito)
│   └── service                # Service Logic Tests (Mocking Ports)
│
├── domain                     # Unit Tests (Pure Java/JUnit 5)
│   └── model                  # Business Rule Tests (e.g., Sign Conversion)
│
├── infrastructure             # Integration Tests (Testcontainers)
│   └── adapters
│       └── web                # Controller IT (Full Context + Docker)
│
└── TransactionApiApplicationTests.java # Smoke Test (H2)
```

## 🐳 How to Run (Docker)
The application is fully dockerized. To run it, ensure you have Docker and Docker Compose installed.

1. Clone the repository and navigate to the root folder.

2. Run the application stack:
    ```
    docker-compose up --build
    ```
3. The API will start on port 8080.
    - API Base URL: http://localhost:8080
    - Swagger UI: http://localhost:8080/swagger-ui.html

## 📦 How to Run (Local)

If you prefer to run it without Docker (requires Java 17+ installed):

```
./mvnw spring-boot:run
```

Note: You will need a local PostgreSQL instance running or configure the application to use H2 profile.

## 🧪 Testing

To execute the test suite (Unit + Integration with Testcontainers):

```Bash
./mvnw clean verify
```

## 📚 API Endpoints

### 1. Accounts
* **Create Account** `POST /accounts`
    ```json
    {
      "document_number": "12345678900"
    }
    ```
* **Get Account** `GET /accounts/{accountId}`

### 2. Transactions
* **Create Transaction** `POST /transactions`
    ```json
    {
      "account_id": 1,
      "operation_type_id": 1,
      "amount": 123.45
    }
    ```

### Operation Type Reference 

| ID | Description          | Sign Logic Stored in DB |
|:-: |:---------------------|:-------------|
|  1 | PURCHASE             | Negative (-) |
|  2 | INSTALLMENT PURCHASE | Negative (-) |
|  3 | WITHDRAWAL           | Negative (-) |
|  4 | PAYMENT              | Positive (+) |

### 📝 Design Decisions

1. Domain Isolation: The domain package has no dependency on Spring or JPA. This ensures that business rules (like the sign conversion logic in Transaction.java) are framework-agnostic.

2. Immutability: Domain models use final attributes where possible to guarantee thread safety and state consistency.

3. Integration Testing: Testcontainers was used to spin up a real PostgreSQL database during tests, ensuring that the SQL queries and JPA mappings work correctly in a production-like environment.

4. Error Handling: A GlobalExceptionHandler manages exceptions (like AccountNotFoundException or DataIntegrityViolationException) to return clean, standardized HTTP 422/400/404 responses.
