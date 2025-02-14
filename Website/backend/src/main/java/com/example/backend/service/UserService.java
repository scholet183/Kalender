package com.example.backend.service;

import com.example.backend.eto.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User getUserByID(int id);

    List<User> getAllUsers();
}
