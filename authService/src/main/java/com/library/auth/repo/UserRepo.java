package com.library.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.auth.model.User;

public interface UserRepo extends JpaRepository<User, String>{
	Optional<User> findByUserName(String userName);

}
