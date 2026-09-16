package com.umarsyed.upimesh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "transactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_transactions_packet_hash",
                        columnNames = "packet_hash"
                )
        }
)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "packet_hash", nullable = false, length = 64)
    private String packetHash;

    @Column(name = "sender_vpa", nullable = false)
    private String senderVpa;

    @Column(name = "receiver_vpa", nullable = false)
    private String receiverVpa;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "settled_at", nullable = false)
    private Instant settledAt;

    @Column(name = "bridge_node_id", nullable = false)
    private String bridgeNodeId;

    @Column(name = "hop_count", nullable = false)
    private int hopCount;

    public Transaction() {
    }

    public Transaction(
            String packetHash,
            String senderVpa,
            String receiverVpa,
            BigDecimal amount,
            Instant settledAt,
            String bridgeNodeId,
            int hopCount) {

        this.packetHash = packetHash;
        this.senderVpa = senderVpa;
        this.receiverVpa = receiverVpa;
        this.amount = amount;
        this.settledAt = settledAt;
        this.bridgeNodeId = bridgeNodeId;
        this.hopCount = hopCount;
    }

    public Long getId() {
        return id;
    }

    public String getPacketHash() {
        return packetHash;
    }

    public String getSenderVpa() {
        return senderVpa;
    }

    public String getReceiverVpa() {
        return receiverVpa;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getSettledAt() {
        return settledAt;
    }

    public String getBridgeNodeId() {
        return bridgeNodeId;
    }

    public int getHopCount() {
        return hopCount;
    }
}