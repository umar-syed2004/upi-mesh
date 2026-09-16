package com.umarsyed.upimesh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(nullable = false, unique = true)
    private String vpa;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private String pinHash;

    @Version
    private long version;

    public Account() {
    }

    public Account(
            String vpa,
            String displayName,
            BigDecimal balance,
            String pinHash) {

        this.vpa = vpa;
        this.displayName = displayName;
        this.balance = balance;
        this.pinHash = pinHash;
    }

    public String getVpa() {
        return vpa;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getPinHash() {
        return pinHash;
    }

    public long getVersion() {
        return version;
    }

    public boolean pinMatches(String suppliedPinHash) {
        return pinHash.equals(suppliedPinHash);
    }

    public void debit(BigDecimal amount) {

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Debit amount must be greater than zero"
            );
        }

        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance");
        }

        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Credit amount must be greater than zero"
            );
        }

        balance = balance.add(amount);
    }
}