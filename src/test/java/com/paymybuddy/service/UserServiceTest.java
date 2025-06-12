package com.paymybuddy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        List<DBUser> users = new ArrayList<>();
        users.add(new DBUser());
        when(userRepository.findAll()).thenReturn(users);

        List<DBUser> result = userService.getUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById() {
        DBUser user = new DBUser();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<DBUser> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        verify(userRepository).findById(1);
    }

    @Test
    void testAddUser() {
        DBUser user = new DBUser();
        user.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(DBUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DBUser savedUser = userService.addUser(user);

        assertEquals("encodedPassword", savedUser.getPassword());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(user);
    }

    @Test
    void testEditUser() {
        DBUser existingUser = new DBUser();
        existingUser.setId(1);
        existingUser.setUsername("old");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPass");

        DBUser updateUser = new DBUser();
        updateUser.setUsername("new");
        updateUser.setEmail("new@example.com");
        updateUser.setPassword("newPass");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(DBUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<DBUser> result = userService.editUser(1, updateUser);

        assertTrue(result.isPresent());
        DBUser updated = result.get();
        assertEquals("new", updated.getUsername());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals("encodedNewPass", updated.getPassword());

        verify(userRepository).findById(1);
        verify(passwordEncoder).encode("newPass");
        verify(userRepository).save(existingUser);
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void testAddFriend_Success() {
        DBUser user = new DBUser();
        user.setId(1);
        user.setFriends(new ArrayList<>());

        DBUser friend = new DBUser();
        friend.setId(2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(friend));
        when(userRepository.save(user)).thenReturn(user);

        Optional<DBUser> result = userService.addFriend(1, 2);

        assertTrue(result.isPresent());
        assertTrue(user.getFriends().contains(friend));
        verify(userRepository).save(user);
    }

    @Test
    void testAddFriend_ThrowsWhenAddingSelf() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addFriend(1, 1);
        });

        assertEquals("Impossible de s'ajouter soi-même en ami.", exception.getMessage());
    }

    @Test
    void testAddFriend_ThrowsWhenFriendNotExist() {
        when(userRepository.findById(1)).thenReturn(Optional.of(new DBUser()));
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addFriend(1, 99);
        });

        assertEquals("La personne ajouté n'existe pas", exception.getMessage());
    }

    @Test
    void testAddFriend_ThrowsWhenFriendAlreadyAdded() {
        DBUser friend = new DBUser();
        friend.setId(2);

        DBUser user = new DBUser();
        user.setId(1);
        List<DBUser> friends = new ArrayList<>();
        friends.add(friend);
        user.setFriends(friends);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addFriend(1, 2);
        });

        assertEquals("Vous avez déjà ajouté cet ami.", exception.getMessage());
    }
}
