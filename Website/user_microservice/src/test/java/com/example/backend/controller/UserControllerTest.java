package com.example.backend.controller;

import com.example.backend.dto.UserDTO;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(UserServiceTestConfiguration.class)  // Importiere die Testkonfiguration mit dem gemockten UserService
@AutoConfigureMockMvc(addFilters = false)      // Sicherheitsfilter deaktivieren
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService; // Der in der Testkonfiguration registrierte Mock

    @Test
    void testSaveUser() throws Exception {
        UserDTO inputDto = new UserDTO();
        inputDto.setName("John Doe");
        inputDto.setEmail("john@example.com");

        UserDTO savedDto = new UserDTO();
        savedDto.setId(1);
        savedDto.setName("John Doe");
        savedDto.setEmail("john@example.com");

        Mockito.when(userService.saveUser(any(UserDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDTO user1 = new UserDTO();
        user1.setId(1);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        UserDTO user2 = new UserDTO();
        user2.setId(2);
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        List<UserDTO> users = Arrays.asList(user1, user2);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));
    }

    @Test
    void testGetUserByID() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setId(1);
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");

        Mockito.when(userService.getUserByID(eq(1))).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDTO updateDto = new UserDTO();
        updateDto.setName("John Updated");
        updateDto.setEmail("john.updated@example.com");
        updateDto.setPassword("password");

        UserDTO updatedUser = new UserDTO();
        updatedUser.setId(1);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");
        updatedUser.setPassword("password");

        Mockito.when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // Für deleteUser() wird in der Service-Schicht kein Rückgabewert erwartet.
        // Wir konfigurieren den Mock so, dass er nichts tut.
        Mockito.doNothing().when(userService).deleteUser(eq(1));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateUserName() throws Exception {
        UserDTO updateNameDto = new UserDTO();
        updateNameDto.setName("John Newname");
        // Der Service-Mock für updateUserName() gibt nichts zurück (void-Methode).
        Mockito.doNothing().when(userService).updateUserName(eq(1), any(UserDTO.class));

        mockMvc.perform(patch("/api/users/update-name/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNameDto)))
                .andExpect(status().isNoContent());
    }
}
