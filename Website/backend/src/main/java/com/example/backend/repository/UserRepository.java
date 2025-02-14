package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.eto.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
