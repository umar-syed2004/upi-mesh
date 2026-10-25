
package com.umarsyed.upimesh.controller;

import com.umarsyed.upimesh.model.Transaction;
import com.umarsyed.upimesh.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Transaction> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @PostMapping
    public Transaction createPayment(
            @Valid @RequestBody PaymentRequest request) {

        return paymentService.transfer(
                request.getSenderVpa(),
                request.getReceiverVpa(),
                request.getAmount(),
                request.getSuppliedPinHash(),
                request.getPacketHash(),
                request.getBridgeNodeId(),
                request.getHopCount()
        );

    }
}
