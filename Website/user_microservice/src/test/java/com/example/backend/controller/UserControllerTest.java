package com.example.backend.controller;

import com.example.backend.assembler.UserModelAssembler;
import com.example.backend.dto.UserDTO;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserModelAssembler assembler;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSaveUser() throws Exception {
        UserDTO userDTO = new UserDTO(1, "John", "john@example.com", "password");

        // Simuliere das Speichern und die Assemblierung
        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDTO);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        when(assembler.toModel(userDTO)).thenReturn(entityModel);

        mockMvc.perform(post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                // Prüfe, ob die ID im zurückgegebenen JSON enthalten ist.
                .andExpect(jsonPath("$.id").value(userDTO.getId()));
    }

    @Test
    public void testGetUser() throws Exception {
        int userId = 1;
        UserDTO userDTO = new UserDTO(userId, "John", "john@example.com", "password");

        when(userService.getUserByID(userId)).thenReturn(userDTO);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        when(assembler.toModel(userDTO)).thenReturn(entityModel);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    public void testUpdateUser() throws Exception {
        int userId = 1;
        UserDTO updatedUser = new UserDTO(userId, "John Updated", "john@example.com", "password");

        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(updatedUser);
        EntityModel<UserDTO> entityModel = EntityModel.of(updatedUser);
        when(assembler.toModel(updatedUser)).thenReturn(entityModel);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        int userId = 1;
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }

    @Test
    public void testUpdateUserName() throws Exception {
        int userId = 1;
        UserDTO userDTO = new UserDTO(userId, "Neuer Name", "john@example.com", "password");

        doNothing().when(userService).updateUserName(eq(userId), any(UserDTO.class));

        mockMvc.perform(patch("/api/users/update-name/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNoContent());

        verify(userService).updateUserName(eq(userId), any(UserDTO.class));
    }

    @Test
    public void testLogin() throws Exception {
        UserDTO userDTO = new UserDTO(1, "John", "john@example.com", "password");

        when(userService.login(any(UserDTO.class))).thenReturn(userDTO);
        EntityModel<UserDTO> entityModel = EntityModel.of(userDTO);
        when(assembler.toModel(userDTO)).thenReturn(entityModel);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));
    }
}
