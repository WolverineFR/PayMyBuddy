package com.paymybuddy.security;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

class AuthenticationSuccessListenerTest {

    @Test
    void testOnAuthenticationSuccess() {
        AuthenticationSuccessListener listener = new AuthenticationSuccessListener();
        AuthenticationSuccessEvent event = mock(AuthenticationSuccessEvent.class);
        Authentication authentication = mock(Authentication.class);

        when(event.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");

        listener.onAuthenticationSuccess(event);

    }
}
