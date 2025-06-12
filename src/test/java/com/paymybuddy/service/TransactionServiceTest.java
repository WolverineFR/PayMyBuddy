package com.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAll();

        assertEquals(1, result.size());
        verify(transactionRepository).findAll();
    }

    @Test
    void testGetTransactionById() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.getTransactionById(1);

        assertTrue(result.isPresent());
        verify(transactionRepository).findById(1);
    }

    @Test
    void testAddTransaction_Success() {
        DBUser sender = new DBUser();
        sender.setId(1);
        sender.setBalance(new BigDecimal("1000"));

        DBUser receiver = new DBUser();
        receiver.setId(2);
        receiver.setBalance(new BigDecimal("500"));

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderId(1);
        dto.setReceiverId(2);
        dto.setAmount(new BigDecimal("100"));
        dto.setDescription("Test transaction");

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(42); // simulate DB id
            return t;
        });
        when(userRepository.save(any(DBUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.addTransaction(dto);

        BigDecimal expectedFee = new BigDecimal("0.5"); // 0.5% de 100 = 0.5
        BigDecimal expectedTotal = new BigDecimal("100.5");

        // Verify balances updated correctly
        BigDecimal expectedBalance = new BigDecimal("899.5");
        assertTrue(sender.getBalance().compareTo(expectedBalance) == 0);
        BigDecimal expectedBalance2 = new BigDecimal("600");
        assertTrue(receiver.getBalance().compareTo(new BigDecimal("600")) == 0);

        // Verify transaction fields
        assertEquals(sender, transaction.getSender());
        assertEquals(receiver, transaction.getReceiver());
        assertEquals(dto.getAmount(), transaction.getAmount());
        assertEquals(0, expectedFee.compareTo(transaction.getFee()));
        assertEquals("Test transaction", transaction.getDescription());
        assertNotNull(transaction.getTimestamp());
        assertEquals(42, transaction.getId());

        verify(userRepository, times(2)).save(any(DBUser.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testAddTransaction_Throws_WhenSenderNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderId(1);
        dto.setReceiverId(2);
        dto.setAmount(new BigDecimal("100"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.addTransaction(dto);
        });

        assertEquals("Sender not found", exception.getMessage());
    }

    @Test
    void testAddTransaction_Throws_WhenReceiverNotFound() {
        DBUser sender = new DBUser();
        sender.setId(1);
        sender.setBalance(new BigDecimal("1000"));

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderId(1);
        dto.setReceiverId(2);
        dto.setAmount(new BigDecimal("100"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.addTransaction(dto);
        });

        assertEquals("Receiver not found", exception.getMessage());
    }

    @Test
    void testAddTransaction_Throws_WhenInsufficientFunds() {
        DBUser sender = new DBUser();
        sender.setId(1);
        sender.setBalance(new BigDecimal("50")); // trop faible

        DBUser receiver = new DBUser();
        receiver.setId(2);
        receiver.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));

        TransactionDTO dto = new TransactionDTO();
        dto.setSenderId(1);
        dto.setReceiverId(2);
        dto.setAmount(new BigDecimal("100"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.addTransaction(dto);
        });

        assertEquals("Fonds insuffisant", exception.getMessage());
    }
}
