package com.paymybuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

/**
 * Implémentation personnalisée de UserDetailsService pour l'authentification Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
     * Charge un utilisateur par son email (nom d'utilisateur pour Spring Security).
     * 
     * @param email l'adresse e-mail de l'utilisateur
     * @return UserDetails utilisé par Spring Security pour l'authentification
     * @throws UsernameNotFoundException si l'utilisateur n'est pas trouvé
     */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		DBUser user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + email);
		}
		return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities(user.getRole()));
	}

	 /**
     * Convertit un rôle en liste d'autorités pour Spring Security.
     * 
     * @param role le rôle de l'utilisateur (ex: "USER")
     * @return liste des GrantedAuthority correspondants
     */
	private List<GrantedAuthority> getGrantedAuthorities(String role) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		return authorities;
	}
}
