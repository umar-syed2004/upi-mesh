package com.umarsyed.upimesh.service;

import com.umarsyed.upimesh.model.Account;
import com.umarsyed.upimesh.model.Transaction;
import com.umarsyed.upimesh.repository.AccountRepository;
import com.umarsyed.upimesh.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    void shouldTransferMoneySuccessfully() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("5000.00"),
                "1234"
        );

        Account bob = new Account(
                "bob@upi",
                "Bob",
                new BigDecimal("2000.00"),
                "5678"
        );

        when(transactionRepository.existsByPacketHash("packet-001"))
                .thenReturn(false);

        when(accountRepository.findById("alice@upi"))
                .thenReturn(Optional.of(alice));

        when(accountRepository.findById("bob@upi"))
                .thenReturn(Optional.of(bob));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Transaction transaction = paymentService.transfer(
                "alice@upi",
                "bob@upi",
                new BigDecimal("500.00"),
                "1234",
                "packet-001",
                "bridge-node-01",
                2
        );

        // Assert
        assertEquals(
                new BigDecimal("4500.00"),
                alice.getBalance()
        );

        assertEquals(
                new BigDecimal("2500.00"),
                bob.getBalance()
        );

        assertEquals(
                "alice@upi",
                transaction.getSenderVpa()
        );

        assertEquals(
                "bob@upi",
                transaction.getReceiverVpa()
        );

        assertEquals(
                new BigDecimal("500.00"),
                transaction.getAmount()
        );

        assertEquals(
                "packet-001",
                transaction.getPacketHash()
        );

        assertEquals(
                "bridge-node-01",
                transaction.getBridgeNodeId()
        );

        assertEquals(
                2,
                transaction.getHopCount()
        );

        verify(accountRepository).save(alice);
        verify(accountRepository).save(bob);
        verify(transactionRepository).save(any(Transaction.class));
    }
    @Test
    void shouldRejectTransferWhenBalanceIsInsufficient() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("500.00"),
                "1234"
        );

        Account bob = new Account(
                "bob@upi",
                "Bob",
                new BigDecimal("2000.00"),
                "5678"
        );

        when(transactionRepository.existsByPacketHash("packet-002"))
                .thenReturn(false);

        when(accountRepository.findById("alice@upi"))
                .thenReturn(Optional.of(alice));

        when(accountRepository.findById("bob@upi"))
                .thenReturn(Optional.of(bob));

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> paymentService.transfer(
                        "alice@upi",
                        "bob@upi",
                        new BigDecimal("1000.00"),
                        "1234",
                        "packet-002",
                        "bridge-node-01",
                        2
                )
        );

        // Verify balances did NOT change
        assertEquals(
                new BigDecimal("500.00"),
                alice.getBalance()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                bob.getBalance()
        );

        // Verify no account was saved
        verify(accountRepository, never()).save(any(Account.class));

        // Verify no transaction was saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void shouldRejectTransferWhenPinIsIncorrect() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("5000.00"),
                "1234"
        );

        Account bob = new Account(
                "bob@upi",
                "Bob",
                new BigDecimal("2000.00"),
                "5678"
        );

        when(transactionRepository.existsByPacketHash("packet-003"))
                .thenReturn(false);

        when(accountRepository.findById("alice@upi"))
                .thenReturn(Optional.of(alice));

        when(accountRepository.findById("bob@upi"))
                .thenReturn(Optional.of(bob));

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.transfer(
                        "alice@upi",
                        "bob@upi",
                        new BigDecimal("500.00"),
                        "9999",       // WRONG PIN
                        "packet-003",
                        "bridge-node-01",
                        2
                )
        );

        // Verify balances did NOT change
        assertEquals(
                new BigDecimal("5000.00"),
                alice.getBalance()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                bob.getBalance()
        );

        // Verify no account was saved
        verify(accountRepository, never()).save(any(Account.class));

        // Verify no transaction was saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void shouldRejectDuplicatePaymentPacket() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("5000.00"),
                "1234"
        );

        Account bob = new Account(
                "bob@upi",
                "Bob",
                new BigDecimal("2000.00"),
                "5678"
        );

        when(transactionRepository.existsByPacketHash("packet-004"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                IllegalStateException.class,
                () -> paymentService.transfer(
                        "alice@upi",
                        "bob@upi",
                        new BigDecimal("500.00"),
                        "1234",
                        "packet-004",
                        "bridge-node-01",
                        2
                )
        );

        // Verify balances did NOT change
        assertEquals(
                new BigDecimal("5000.00"),
                alice.getBalance()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                bob.getBalance()
        );

        // No account should be saved
        verify(accountRepository, never()).save(any(Account.class));

        // No new transaction should be saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void shouldRejectTransferWhenSenderDoesNotExist() {

        // Arrange
        when(transactionRepository.existsByPacketHash("packet-005"))
                .thenReturn(false);

        when(accountRepository.findById("unknown@upi"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.transfer(
                        "unknown@upi",
                        "bob@upi",
                        new BigDecimal("500.00"),
                        "1234",
                        "packet-005",
                        "bridge-node-01",
                        2
                )
        );

        // Verify sender lookup happened
        verify(accountRepository).findById("unknown@upi");

        // No account should be saved
        verify(accountRepository, never()).save(any(Account.class));

        // No transaction should be saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void shouldRejectTransferWhenReceiverDoesNotExist() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("5000.00"),
                "1234"
        );

        when(transactionRepository.existsByPacketHash("packet-006"))
                .thenReturn(false);

        when(accountRepository.findById("alice@upi"))
                .thenReturn(Optional.of(alice));

        when(accountRepository.findById("unknown@upi"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.transfer(
                        "alice@upi",
                        "unknown@upi",
                        new BigDecimal("500.00"),
                        "1234",
                        "packet-006",
                        "bridge-node-01",
                        2
                )
        );

        // Verify sender and receiver were searched
        verify(accountRepository).findById("alice@upi");
        verify(accountRepository).findById("unknown@upi");

        // Alice's balance should NOT change
        assertEquals(
                new BigDecimal("5000.00"),
                alice.getBalance()
        );

        // No account should be saved
        verify(accountRepository, never()).save(any(Account.class));

        // No transaction should be saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void shouldRejectTransferWhenAmountIsZero() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("5000.00"),
                "1234"
        );

        Account bob = new Account(
                "bob@upi",
                "Bob",
                new BigDecimal("2000.00"),
                "5678"
        );

        when(transactionRepository.existsByPacketHash("packet-007"))
                .thenReturn(false);

        when(accountRepository.findById("alice@upi"))
                .thenReturn(Optional.of(alice));

        when(accountRepository.findById("bob@upi"))
                .thenReturn(Optional.of(bob));

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.transfer(
                        "alice@upi",
                        "bob@upi",
                        new BigDecimal("0.00"),
                        "1234",
                        "packet-007",
                        "bridge-node-01",
                        2
                )
        );

        // Verify balances did NOT change
        assertEquals(
                new BigDecimal("5000.00"),
                alice.getBalance()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                bob.getBalance()
        );

        // No account should be saved
        verify(accountRepository, never()).save(any(Account.class));

        // No transaction should be saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    @Test
    void shouldRejectTransferWhenAmountIsNegative() {

        // Arrange
        Account alice = new Account(
                "alice@upi",
                "Alice",
                new BigDecimal("5000.00"),
                "1234"
        );

        Account bob = new Account(
                "bob@upi",
                "Bob",
                new BigDecimal("2000.00"),
                "5678"
        );

        when(transactionRepository.existsByPacketHash("packet-008"))
                .thenReturn(false);

        when(accountRepository.findById("alice@upi"))
                .thenReturn(Optional.of(alice));

        when(accountRepository.findById("bob@upi"))
                .thenReturn(Optional.of(bob));

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.transfer(
                        "alice@upi",
                        "bob@upi",
                        new BigDecimal("-500.00"),
                        "1234",
                        "packet-008",
                        "bridge-node-01",
                        2
                )
        );

        // Verify balances did NOT change
        assertEquals(
                new BigDecimal("5000.00"),
                alice.getBalance()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                bob.getBalance()
        );

        // No account should be saved
        verify(accountRepository, never()).save(any(Account.class));

        // No transaction should be saved
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
