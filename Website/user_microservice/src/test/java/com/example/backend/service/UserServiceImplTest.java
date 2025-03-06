package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.eto.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest  // Loads the entire Spring application context for integration testing
@Transactional   // Each test is run in a transaction that will be rolled back after the test
public class UserServiceImplTest {

    @Autowired
    private UserService userService;  // The service under test

    @Autowired
    private UserRepository userRepository;  // Direct access to the repository for additional verification

    /**
     * Test for saving a user.
     */
    @Test
    public void testSaveUser() {
        // Create a new UserDTO object with sample data
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Jane Doe");
        userDTO.setEmail("jane@example.com");
        userDTO.setPassword("password");

        // Save the user via the service
        UserDTO savedUser = userService.saveUser(userDTO);

        // Verify that an ID is generated and that the saved data matches the input
        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("Jane Doe", savedUser.getName());
        Assertions.assertEquals("jane@example.com", savedUser.getEmail());

        // Optionally, verify directly via the repository that the user was persisted
        User userFromDb = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Assertions.assertEquals("Jane Doe", userFromDb.getName());
    }

    /**
     * Test for updating an existing user.
     */
    @Test
    public void testUpdateUser() {
        // First, create and save a user
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Mark");
        userDTO.setEmail("mark@example.com");
        userDTO.setPassword("password");
        UserDTO savedUser = userService.saveUser(userDTO);

        // Create an update DTO with the new details
        UserDTO updateDto = new UserDTO();
        updateDto.setName("Mark Updated");
        updateDto.setEmail("mark.updated@example.com");

        // Update the user using the service
        UserDTO updatedUser = userService.updateUser(savedUser.getId(), updateDto);

        // Verify that the updated user has the new data
        Assertions.assertEquals("Mark Updated", updatedUser.getName());
        Assertions.assertEquals("mark.updated@example.com", updatedUser.getEmail());
    }

    /**
     * Test for retrieving all users.
     */
    @Test
    public void testGetAllUsers() {
        // Create and save multiple users
        UserDTO user1 = new UserDTO();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");
        user1.setPassword("password");
        userService.saveUser(user1);

        UserDTO user2 = new UserDTO();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");
        user2.setPassword("password");
        userService.saveUser(user2);

        // Retrieve all users via the service
        List<UserDTO> allUsers = userService.getAllUsers();

        // Check that there are at least two users in the returned list
        Assertions.assertTrue(allUsers.size() >= 2, "There should be at least two users");

        // Optionally, verify that users named "Alice" and "Bob" are present
        boolean foundAlice = allUsers.stream().anyMatch(u -> "Alice".equals(u.getName()));
        boolean foundBob = allUsers.stream().anyMatch(u -> "Bob".equals(u.getName()));
        Assertions.assertTrue(foundAlice, "Alice should be present");
        Assertions.assertTrue(foundBob, "Bob should be present");
    }

    /**
     * Test for retrieving a user by ID.
     */
    @Test
    public void testGetUserById() {
        // Create and save a user
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Charlie");
        userDTO.setEmail("charlie@example.com");
        userDTO.setPassword("password");
        UserDTO savedUser = userService.saveUser(userDTO);

        // Retrieve the user using the generated ID
        UserDTO fetchedUser = userService.getUserByID(savedUser.getId());

        // Verify that the retrieved user matches the saved one
        Assertions.assertNotNull(fetchedUser);
        Assertions.assertEquals(savedUser.getId(), fetchedUser.getId());
        Assertions.assertEquals("Charlie", fetchedUser.getName());
        Assertions.assertEquals("charlie@example.com", fetchedUser.getEmail());
    }

    /**
     * Test for deleting a user.
     */
    @Test
    public void testDeleteUser() {
        // Create and save a user
        UserDTO userDTO = new UserDTO();
        userDTO.setName("David");
        userDTO.setEmail("david@example.com");
        userDTO.setPassword("password");
        UserDTO savedUser = userService.saveUser(userDTO);

        // Delete the user via the service
        userService.deleteUser(savedUser.getId());

        // Verify that the user is no longer present in the repository
        Assertions.assertFalse(userRepository.findById(savedUser.getId()).isPresent(),
                "User should no longer exist after deletion");
    }

    /**
     * Test for updating only the user's name.
     */
    @Test
    public void testUpdateUserName() {
        // Create and save a user
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Eve");
        userDTO.setEmail("eve@example.com");
        userDTO.setPassword("password");
        UserDTO savedUser = userService.saveUser(userDTO);

        // Create a DTO with the updated name only
        UserDTO updateNameDto = new UserDTO();
        updateNameDto.setName("Eve Updated");

        // Update the user's name via the service
        userService.updateUserName(savedUser.getId(), updateNameDto);

        // Retrieve the user after the update
        UserDTO updatedUser = userService.getUserByID(savedUser.getId());
        // Verify that the name was updated while the email remains unchanged
        Assertions.assertEquals("Eve Updated", updatedUser.getName());
        Assertions.assertEquals("eve@example.com", updatedUser.getEmail());
    }
}
