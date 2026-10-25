
package com.umarsyed.upimesh.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PaymentRequest {

    @NotBlank(message = "Sender VPA is required")
    private String senderVpa;

    @NotBlank(message = "Receiver VPA is required")
    private String receiverVpa;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "PIN is required")
    private String suppliedPinHash;

    @NotBlank(message = "Packet hash is required")
    private String packetHash;

    @NotBlank(message = "Bridge node ID is required")
    private String bridgeNodeId;

    @PositiveOrZero(message = "Hop count cannot be negative")
    private int hopCount;

    public PaymentRequest() {
    }

    public String getSenderVpa() {
        return senderVpa;
    }

    public void setSenderVpa(String senderVpa) {
        this.senderVpa = senderVpa;
    }

    public String getReceiverVpa() {
        return receiverVpa;
    }

    public void setReceiverVpa(String receiverVpa) {
        this.receiverVpa = receiverVpa;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSuppliedPinHash() {
        return suppliedPinHash;
    }

    public void setSuppliedPinHash(String suppliedPinHash) {
        this.suppliedPinHash = suppliedPinHash;
    }

    public String getPacketHash() {
        return packetHash;
    }

    public void setPacketHash(String packetHash) {
        this.packetHash = packetHash;
    }

    public String getBridgeNodeId() {
        return bridgeNodeId;
    }

    public void setBridgeNodeId(String bridgeNodeId) {
        this.bridgeNodeId = bridgeNodeId;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }
}
