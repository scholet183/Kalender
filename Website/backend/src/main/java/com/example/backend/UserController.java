package com.example.backend;

import com.example.backend.UserService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:4200") // Erlaubt Angular-Anfragen
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

		@GetMapping("/{id}")
		public User getUser(@PathVariable int id) {
				return userService.getUserByID(id);
		}
}
