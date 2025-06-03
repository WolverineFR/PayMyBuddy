package com.paymybuddy.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

@WebMvcTest(SendTransactionController.class)
class SendTransactionControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	private DBUser sender;
	private DBUser receiver;

	@BeforeEach
	void setup() {
		receiver = new DBUser();
		receiver.setEmail("receiver@email.com");
		receiver.setBalance(new BigDecimal("50.00"));

		sender = new DBUser();
		sender.setEmail("sender@email.com");
		sender.setBalance(new BigDecimal("100.00"));
		sender.setFriends(List.of(receiver));
	}

	@Test
	@WithMockUser(username = "sender@email.com", roles = "USER")
	void testSendTransaction_SuccessfulTransaction() throws Exception {

		when(userRepository.findByEmail("sender@email.com")).thenReturn(sender);
		when(userRepository.findByEmail("receiver@email.com")).thenReturn(receiver);

		mockMvc.perform(post("/user/transaction").param("friendEmail", "receiver@email.com")
				.param("description", "Dinner").param("amount", "10.00").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/transaction"))
				.andExpect(flash().attribute("successMessage", "Montant envoyé avec succès !"));

		ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
		verify(transactionRepository).save(transactionCaptor.capture());
		Transaction savedTransaction = transactionCaptor.getValue();

		assert savedTransaction.getSender().equals(sender);
		assert savedTransaction.getReceiver().equals(receiver);
		assert savedTransaction.getAmount().compareTo(new BigDecimal("10.00")) == 0;
		assert savedTransaction.getFee().compareTo(new BigDecimal("0.05")) == 0;

		verify(userRepository).save(sender);
		verify(userRepository).save(receiver);
		assert sender.getBalance().compareTo(new BigDecimal("89.95")) == 0;
		assert receiver.getBalance().compareTo(new BigDecimal("60.00")) == 0;
	}
	
	@Test
	@WithMockUser(username = "sender@email.com", roles = "USER")
	void testSendTransaction_InsufficientFunds() throws Exception {

	    when(userRepository.findByEmail("sender@email.com")).thenReturn(sender);
	    when(userRepository.findByEmail("receiver@email.com")).thenReturn(receiver);

	    mockMvc.perform(post("/user/transaction")
	            .param("friendEmail", "receiver@email.com")
	            .param("description", "Dinner")
	            .param("amount", "100.00")
	            .with(csrf()))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(redirectedUrl("/user/transaction"))
	        .andExpect(flash().attributeExists("errorMessage"));


	    verify(transactionRepository, never()).save(any(Transaction.class));

	    assert sender.getBalance().compareTo(new BigDecimal("100.00")) == 0;
	    assert receiver.getBalance().compareTo(new BigDecimal("50.00")) == 0;
	    
	    verify(userRepository, never()).save(sender);
	    verify(userRepository, never()).save(receiver);
	}

}
