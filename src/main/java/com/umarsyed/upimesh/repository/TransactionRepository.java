package com.umarsyed.upimesh.repository;

import com.umarsyed.upimesh.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    boolean existsByPacketHash(String packetHash);

    Optional<Transaction> findByPacketHash(String packetHash);
}