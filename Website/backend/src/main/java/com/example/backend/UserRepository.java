package com.example.backend;

import org.springframework.data.repository.CrudRepository;
import com.example.backend.User;
import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findAll();
	User findById(int id);
}
