package com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pismo.assessment.transaction_api.infrastructure.adapters.persistence.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {}
