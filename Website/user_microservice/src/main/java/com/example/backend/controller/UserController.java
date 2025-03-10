package com.example.backend.controller;

import com.example.backend.assembler.UserModelAssembler;
import com.example.backend.dto.UserDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler assembler;

    @PostMapping("/save")
    public ResponseEntity<EntityModel<UserDTO>> saveUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.saveUser(userDTO);
        EntityModel<UserDTO> userModel = assembler.toModel(createdUser);
        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUser(createdUser.getId())).toUri())
                .body(userModel);
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getAllUsers() {
        List<EntityModel<UserDTO>> users = userService.getAllUsers().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserDTO>> collectionModel = CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUser(@PathVariable int id) {
        UserDTO userDTO = userService.getUserByID(id);
        return ResponseEntity.ok(assembler.toModel(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(assembler.toModel(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/update-name/{id}")
    public ResponseEntity<Void> updateUserName(@PathVariable int id, @RequestBody UserDTO userDTO) {
        userService.updateUserName(id, userDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<EntityModel<UserDTO>> login(@RequestBody UserDTO userDTO) {
        UserDTO loggedInUser = userService.login(userDTO);
        return ResponseEntity.ok(assembler.toModel(loggedInUser));
    }
}
