package com.paymybuddy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class IntegrationFullTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@BeforeEach
	void cleanDb() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	@WithMockUser(username = "user1@example.com", roles = "USER")
	public void testFullUserFlow() throws Exception {
		// 1. Créer user2 (l'ami)
		DBUser friend = new DBUser();
		friend.setEmail("friend@example.com");
		friend.setUsername("Friend");
		friend.setPassword(passwordEncoder.encode("password2"));
		friend.setRole("USER");
		friend.setBalance(BigDecimal.ZERO);
		userRepository.save(friend);

		// 2. Créer user1 (le principal)
		DBUser user = new DBUser();
		user.setEmail("user1@example.com");
		user.setUsername("User1");
		user.setPassword(passwordEncoder.encode("password1"));
		user.setRole("USER");
		user.setBalance(BigDecimal.ZERO);
		userRepository.save(user);

		// 4. Ajouter user2 en ami (simuler authentifié avec user1)
		mockMvc.perform(post("/user/add-friend").param("email", "friend@example.com")
				.with(user("user1@example.com").password("password1").roles("USER")).with(csrf()))
				.andExpect(status().is3xxRedirection());

		// Vérifier que l'ami est bien ajouté en base
		DBUser updatedUser = userRepository.findByEmail("user1@example.com");
		assertTrue(updatedUser.getFriends().stream().anyMatch(f -> f.getEmail().equals("friend@example.com")));

		// 5. Créditer le compte user1 (ajout d'argent)
		mockMvc.perform(post("/user/profil/balance").param("amount", "100").param("action", "credit")
				.with(user("user1@example.com").roles("USER")).with(csrf())).andExpect(status().is3xxRedirection());

		// 6. Envoyer 50 € à user2
		mockMvc.perform(post("/user/transaction").param("friendEmail", "friend@example.com").param("amount", "50")
				.param("description", "Test").with(user("user1@example.com").roles("USER")).with(csrf()))
				.andExpect(status().is3xxRedirection());

		// Vérification des soldes et transaction
		DBUser sender = userRepository.findByEmail("user1@example.com");
		DBUser receiver = userRepository.findByEmail("friend@example.com");

		// sender balance doit être 100 - 50 - frais (0.005 * 50 = 0.25 arrondi)
		BigDecimal expectedFee = new BigDecimal("0.25");
		BigDecimal expectedSenderBalance = new BigDecimal("100").subtract(new BigDecimal("50").add(expectedFee));
		assertEquals(0, sender.getBalance().compareTo(expectedSenderBalance));

		// receiver balance doit être 50
		assertEquals(0, receiver.getBalance().compareTo(new BigDecimal("50")));

		// Transaction enregistrée
		List<Transaction> transactions = transactionRepository.findAll();
		assertFalse(transactions.isEmpty());
		Transaction tx = transactions.get(0);
		assertEquals("user1@example.com", tx.getSender().getEmail());
		assertEquals("friend@example.com", tx.getReceiver().getEmail());
		assertEquals(0, tx.getAmount().compareTo(new BigDecimal("50")));
		assertEquals(0, tx.getFee().compareTo(expectedFee));
	}

}
