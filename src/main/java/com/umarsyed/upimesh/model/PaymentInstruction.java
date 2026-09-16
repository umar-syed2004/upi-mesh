package com.umarsyed.upimesh.model;

import java.math.BigDecimal;

public class PaymentInstruction {

    private String sender;
    private String receiver;
    private BigDecimal amount;
    private String pinHash;
    private String nonce;
    private long signedAt;

    public PaymentInstruction() {
    }

    public PaymentInstruction(
            String sender,
            String receiver,
            BigDecimal amount,
            String pinHash,
            String nonce,
            long signedAt) {

        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.pinHash = pinHash;
        this.nonce = nonce;
        this.signedAt = signedAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public long getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(long signedAt) {
        this.signedAt = signedAt;
    }
}