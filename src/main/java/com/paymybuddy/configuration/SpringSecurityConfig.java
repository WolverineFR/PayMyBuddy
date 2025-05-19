package com.paymybuddy.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.paymybuddy.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(auth -> 
			auth.requestMatchers("/login", "/register").permitAll()
				.anyRequest().authenticated()
		)
		.formLogin();
		return http.build();
	}
	
	@Bean
	public UserDetailsService users () {
		
		return customUserDetailsService;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder ) throws Exception{
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}
	
	/*
	@Bean
    CommandLineRunner generatePassword() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = "jaimelesfleurs";
            String hashed = encoder.encode(password);
            System.out.println("Mot de passe chiffré : " + hashed);
        };
    }
    */	
	
	/*
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/transaction/**").permitAll()
                .anyRequest().authenticated()            
            )
            .formLogin()  // active le login form par défaut
            .and()
            .csrf().disable();  // à activer selon ton besoin

        return http.build();
    }
    */
}
