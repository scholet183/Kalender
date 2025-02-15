package com.example.backend.service;

import com.example.backend.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    UserDTO getUserByID(int id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(int id, UserDTO userDTO);

    void deleteUser(int id);

    void updateUserName(int id, UserDTO userDTO);
}
