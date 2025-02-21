package com.example.backend.service.impl;

import com.example.backend.dto.UserDTO;

import com.example.backend.eto.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Mapping von Entity zu DTO
    private UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

    // Mapping von DTO zu Entity
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        System.out.println(user.getPassword());
        return user;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        // Prüfe, ob alle erforderlichen Felder vorhanden sind
        if (userDTO.getName() == null || userDTO.getEmail() == null || userDTO.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, E-Mail und Passwort müssen angegeben werden.");
        }

        // Prüfe, ob die E-Mail bereits vergeben ist
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Die E-Mail ist bereits vergeben.");
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public UserDTO getUserByID(int id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id " + id));
        return convertToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(int id, UserDTO userDTO) {
        User existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id " + id));
        // Beispiel: Aktualisierung des Namens (weitere Felder bei Bedarf)
        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id " + id));
        userRepository.delete(user);
    }

    @Override
    public void updateUserName(int id, UserDTO userDTO) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id " + id));
        user.setName(userDTO.getName());
        userRepository.save(user);
    }

    @Override
    public UserDTO login(UserDTO userDTO) {
        // Suche Benutzer per E-Mail (alternativ z. B. per Name, je nach Anforderung)
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ungültige Zugangsdaten"));
        // Prüfe, ob das Passwort übereinstimmt
        if (!user.getPassword().equals(userDTO.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Ungültige Zugangsdaten");
        }
        return convertToDto(user);
    }

}
