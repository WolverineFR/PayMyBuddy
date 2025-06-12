package com.paymybuddy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private DBUser testUser;

    @BeforeEach
    void setup() {
        testUser = new DBUser();
        testUser.setEmail("martin@email.com");
        testUser.setPassword("123");
        testUser.setUsername("martin");
    }

    @Test
    @WithMockUser(username = "martin@email.com" ,roles="USER")
    void testGetRegisterPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeExists("user"))
               .andExpect(model().attributeDoesNotExist("registrationError"));
    }

    @Test
    @WithMockUser(username = "martin@email.com" ,roles="USER")
    void testGetRegisterPageWithError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register")
                .param("error", "true"))
               .andExpect(status().isOk())
               .andExpect(view().name("register"))
               .andExpect(model().attributeExists("registrationError"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "martin@email.com" ,roles="USER")
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .param("email", testUser.getEmail())
                .param("username", testUser.getUsername())
                .param("password", testUser.getPassword())
                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register?error=true"));

        verify(userRepository, never()).save(any(DBUser.class));
    }

    @Test
    @WithMockUser(username = "martin@email.com" ,roles="USER")
    void testRegisterUser_EncodePasswordAndSave() throws Exception {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .param("email", testUser.getEmail())
                .param("username", testUser.getUsername())
                .param("password", testUser.getPassword())
                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"));

        ArgumentCaptor<DBUser> userCaptor = ArgumentCaptor.forClass(DBUser.class);
        verify(userRepository).save(userCaptor.capture());

        DBUser savedUser = userCaptor.getValue();
        assert savedUser.getEmail().equals(testUser.getEmail());
        assert savedUser.getUsername().equals(testUser.getUsername());
        assert savedUser.getPassword().equals("encodedPassword");
        assert savedUser.getRole().equals("USER");
    }
}