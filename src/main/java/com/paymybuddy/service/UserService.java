package com.paymybuddy.service;

import java.util.List;

import com.paymybuddy.model.DBUser;

public interface UserService {
    DBUser getUserByEmail(String email);
    List<DBUser> getAllUsers();
    DBUser saveUser(DBUser user);
}
