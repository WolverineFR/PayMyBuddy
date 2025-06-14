package com.paymybuddy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BalancePageController.class)
class BalancePageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private TransactionService transactionService ;

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void testGetUserBalancePage() throws Exception {
		DBUser user = new DBUser();
		user.setEmail("user@email.com");
		user.setBalance(new BigDecimal("150.50"));

		when(userService.getUserByEmail("user@email.com")).thenReturn(user);

		mockMvc.perform(get("/user/profil/balance")).andExpect(status().isOk()).andExpect(view().name("balance"))
				.andExpect(model().attribute("balance", new BigDecimal("150.50")));
	}

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void testPostCreditBalance() throws Exception {
		DBUser user = new DBUser();
		user.setEmail("user@email.com");
		user.setBalance(new BigDecimal("100"));

		when(userService.getUserByEmail("user@email.com")).thenReturn(user);

		mockMvc.perform(post("/user/profil/balance").param("amount", "50").param("action", "credit").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("balance"))
				.andExpect(flash().attribute("succesMessage", "Compte crédité de 50 €."))
				.andExpect(flash().attribute("balance", new BigDecimal("150")));

		verify(userService).saveUser(user);
	}

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void testPostDebitBalance_SufficientFunds() throws Exception {
		DBUser user = new DBUser();
		user.setEmail("user@email.com");
		user.setBalance(new BigDecimal("100"));

		when(userService.getUserByEmail("user@email.com")).thenReturn(user);

		mockMvc.perform(post("/user/profil/balance").param("amount", "40").param("action", "debit").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("balance"))
				.andExpect(flash().attribute("succesMessage", "Compte débité de 40 €."))
				.andExpect(flash().attribute("balance", new BigDecimal("60")));

		verify(userService).saveUser(user);
	}

	@Test
	@WithMockUser(username = "user@email.com", roles = "USER")
	void testPostDebitBalance_InsufficientFunds() throws Exception {
		DBUser user = new DBUser();
		user.setEmail("user@email.com");
		user.setBalance(new BigDecimal("30"));

		when(userService.getUserByEmail("user@email.com")).thenReturn(user);

		mockMvc.perform(post("/user/profil/balance").param("amount", "50").param("action", "debit").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("balance"))
				.andExpect(flash().attribute("errorMessage", "Fonds insuffisants pour débiter."));

		verify(userService, never()).saveUser(any());
	}

	@Test
	@WithMockUser(username = "admin@email.com", roles = "ADMIN")
	void testGetAdminBalancePage() throws Exception {
		DBUser user1 = new DBUser();
		user1.setEmail("user1@email.com");
		DBUser user2 = new DBUser();
		user2.setEmail("user2@email.com");

		List<DBUser> allUsers = Arrays.asList(user1, user2);

		Transaction t1 = new Transaction();
		t1.setFee(new BigDecimal("1.5"));
		Transaction t2 = new Transaction();
		t2.setFee(new BigDecimal("2.5"));
		Transaction t3 = new Transaction();
		t3.setFee(null);

		List<Transaction> allTransactions = Arrays.asList(t1, t2, t3);

		when(userService.getAllUsers()).thenReturn(allUsers);
		when(transactionService.getAllTransactions()).thenReturn(allTransactions);

		mockMvc.perform(get("/admin/profil/balance")).andExpect(status().isOk()).andExpect(view().name("balance"))
				.andExpect(model().attribute("users", allUsers))
				.andExpect(model().attribute("totalFees", new BigDecimal("4.0")));
	}
}
