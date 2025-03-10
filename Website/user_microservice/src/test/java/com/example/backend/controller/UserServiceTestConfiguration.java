package com.example.backend.controller;

import com.example.backend.assembler.UserModelAssembler;
import com.example.backend.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserServiceTestConfiguration {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public UserModelAssembler userModelAssembler() { return Mockito.mock(UserModelAssembler.class); }
}
