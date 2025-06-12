package com.paymybuddy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

@WebMvcTest(SendTransactionController.class)
class SendTransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@Test
	@WithMockUser(username = "sender@email.com")
	void testShowTransactionPage() throws Exception {
		DBUser sender = new DBUser();
		sender.setId(1);
		sender.setEmail("sender@email.com");
		sender.setUsername("SenderUser");
		sender.setBalance(BigDecimal.TEN);
		sender.setFriends(Collections.emptyList());

		DBUser receiver = new DBUser();
		receiver.setId(2);
		receiver.setEmail("receiver@email.com");
		receiver.setUsername("ReceiverUser");

		Transaction transaction = new Transaction();
		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setAmount(BigDecimal.ONE);

		when(userRepository.findByEmail("sender@email.com")).thenReturn(sender);
		when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));

		mockMvc.perform(get("/user/transaction")).andExpect(status().isOk()).andExpect(view().name("transaction"))
				.andExpect(model().attributeExists("transactions")).andExpect(model().attributeExists("friends"))
				.andExpect(model().attributeExists("balance"));
	}

	@Test
	@WithMockUser(username = "sender@email.com")
	void testSendTransaction_Success() throws Exception {
		DBUser sender = new DBUser();
		sender.setId(1);
		sender.setEmail("sender@email.com");
		sender.setBalance(new BigDecimal("100"));

		DBUser receiver = new DBUser();
		receiver.setId(2);
		receiver.setEmail("receiver@email.com");
		receiver.setBalance(new BigDecimal("50"));

		sender.setFriends(Collections.singletonList(receiver));

		when(userRepository.findByEmail("sender@email.com")).thenReturn(sender);
		when(userRepository.findByEmail("receiver@email.com")).thenReturn(receiver);

		mockMvc.perform(post("/user/transaction").param("friendEmail", "receiver@email.com")
				.param("description", "Remboursement").param("amount", "10").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/transaction"))
				.andExpect(flash().attributeExists("successMessage"));

		verify(transactionRepository).save(any(Transaction.class));
		verify(userRepository).save(sender);
		verify(userRepository).save(receiver);
	}

	@Test
	@WithMockUser(username = "sender@email.com")
	void testSendTransaction_InsufficientFunds() throws Exception {
		DBUser sender = new DBUser();
		sender.setId(1);
		sender.setEmail("sender@email.com");
		sender.setBalance(new BigDecimal("5"));

		DBUser receiver = new DBUser();
		receiver.setId(2);
		receiver.setEmail("receiver@email.com");
		receiver.setBalance(new BigDecimal("50"));
		
		sender.setFriends(Collections.singletonList(receiver));

		when(userRepository.findByEmail("sender@email.com")).thenReturn(sender);
		when(userRepository.findByEmail("receiver@email.com")).thenReturn(receiver);

		mockMvc.perform(post("/user/transaction").param("friendEmail", "receiver@email.com")
				.param("description", "Remboursement").param("amount", "10").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/transaction"))
				.andExpect(flash().attributeExists("errorMessage"));

		verify(transactionRepository, never()).save(any());
	}

	@Test
	@WithMockUser(username = "sender@email.com")
	void testSendTransaction_ToNonFriend_ShouldFail() throws Exception {
		DBUser sender = new DBUser();
		sender.setId(1);
		sender.setEmail("sender@email.com");
		sender.setBalance(new BigDecimal("100"));

		DBUser receiver = new DBUser();
		receiver.setId(2);
		receiver.setEmail("receiver@email.com");
		receiver.setBalance(new BigDecimal("50"));

		sender.setFriends(Collections.emptyList());

		when(userRepository.findByEmail("sender@email.com")).thenReturn(sender);
		when(userRepository.findByEmail("receiver@email.com")).thenReturn(receiver);

		mockMvc.perform(post("/user/transaction").param("friendEmail", "receiver@email.com")
				.param("description", "Paiement test").param("amount", "10").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/transaction"))
				.andExpect(flash().attribute("errorMessage", "Destinataire non valide"));

		verify(transactionRepository, never()).save(any());
		verify(userRepository, never()).save(any());
	}

}
