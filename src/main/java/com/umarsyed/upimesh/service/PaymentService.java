
        package com.umarsyed.upimesh.service;

import com.umarsyed.upimesh.model.Account;
import com.umarsyed.upimesh.model.Transaction;
import com.umarsyed.upimesh.repository.AccountRepository;
import com.umarsyed.upimesh.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class PaymentService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public PaymentService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction transfer(
            String senderVpa,
            String receiverVpa,
            BigDecimal amount,
            String suppliedPinHash,
            String packetHash,
            String bridgeNodeId,
            int hopCount) {

        // Prevent duplicate payment packets
        if (transactionRepository.existsByPacketHash(packetHash)) {
            throw new IllegalStateException(
                    "This payment packet was already processed"
            );
        }

        // Find sender account
        Account sender = accountRepository.findById(senderVpa)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sender account was not found"
                        )
                );

        // Find receiver account
        Account receiver = accountRepository.findById(receiverVpa)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Receiver account was not found"
                        )
                );

        // Verify sender PIN
        if (!sender.pinMatches(suppliedPinHash)) {
            throw new IllegalArgumentException("Incorrect PIN");
        }

        // Transfer money between accounts
        sender.debit(amount);
        receiver.credit(amount);

        // Save updated account balances
        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Create transaction record
        Transaction transaction = new Transaction(
                packetHash,
                senderVpa,
                receiverVpa,
                amount,
                Instant.now(),
                bridgeNodeId,
                hopCount
        );

        // Save and return transaction
        return transactionRepository.save(transaction);
    }

    // Retrieve all payment history
    @Transactional(readOnly = true)
    public List<Transaction> getAllPayments() {
        return transactionRepository.findAll();
    }
}