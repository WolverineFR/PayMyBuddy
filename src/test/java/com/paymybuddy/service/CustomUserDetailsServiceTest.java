package com.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CustomUserDetailsService userDetailsService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void loadUserByUsername_UserExists_ReturnsUserDetails() {
		// Arrange
		DBUser mockUser = new DBUser();
		mockUser.setEmail("test@example.com");
		mockUser.setPassword("password123");
		mockUser.setRole("USER");

		when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

		// Act
		UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

		// Assert
		assertNotNull(userDetails);
		assertEquals("test@example.com", userDetails.getUsername());
		assertEquals("password123", userDetails.getPassword());

		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		assertEquals(1, authorities.size());
		assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void loadUserByUsername_UserNotFound_ThrowsException() {
		// Arrange
		when(userRepository.findByEmail("unknown@example.com")).thenReturn(null);

		// Act & Assert
		UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class,
				() -> userDetailsService.loadUserByUsername("unknown@example.com"));

		assertEquals("User not found with username: unknown@example.com", thrown.getMessage());
		verify(userRepository, times(1)).findByEmail("unknown@example.com");
	}
}
