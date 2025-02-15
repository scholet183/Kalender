package com.example.backend.controller;

import com.example.backend.dto.UserDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        UserDTO userDTO = userService.getUserByID(id);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-name/{id}")
    public ResponseEntity<Void> updateUserName(@PathVariable int id, @RequestBody UserDTO userDTO) {
        userService.updateUserName(id, userDTO);
        return ResponseEntity.noContent().build();
    }
}
